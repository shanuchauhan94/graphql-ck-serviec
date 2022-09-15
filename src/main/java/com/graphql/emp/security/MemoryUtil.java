package com.graphql.emp.security;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Stack;

@Slf4j
public class MemoryUtil {

    public MemoryUtil() {
    }

    private static Unsafe UNSAFE;
    private static final Object HELPER_OBJECT = new Object() {
        byte b;
    };
    private static long OBJECT_BASE_SIZE; // 8 for 32-bit, 12 for 64-bit compressed oops, 16 for 64-bit
    private static int ADDRESS_SIZE; // size of native pointer 4 32-bit, 8 64-bit
    private static int REFERENCE_SIZE;// size of java reference 4 32-bit, 4 64-bit compressed 8 for 64-bit
    private static com.graphql.emp.security.AddressMode ADDRESS_MODE;
    private static final int OBJECT_ALIGNMENT = 8;

    static {
        try {
            // use reflection to get a reference to the 'Unsafe' object
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            OBJECT_BASE_SIZE = UNSAFE.objectFieldOffset(HELPER_OBJECT.getClass().getDeclaredField("b"));
            ADDRESS_SIZE = UNSAFE.addressSize();
            REFERENCE_SIZE = UNSAFE.arrayIndexScale(Object[].class);

            if (ADDRESS_SIZE == 4) {
                ADDRESS_MODE = AddressMode.MEM_32BIT;
            } else if (ADDRESS_SIZE == 8 && REFERENCE_SIZE == 8) {
                ADDRESS_MODE = AddressMode.MEM_64BIT;
            } else if (ADDRESS_SIZE == 8 && REFERENCE_SIZE == 4) {
                ADDRESS_MODE = AddressMode.MEM_64BIT_COMPRESSED_OOPS;
            } else {
                ADDRESS_MODE = AddressMode.UNKNOWN;
            }
        } catch (Exception e) {
            log.error("error in memory utils {} ", e.getMessage());
        }
    }

    // Return the size of the object excluding any reference objects
    public static long shallowSizeOf(final Object object) {

        Class<?> objectClass = object.getClass();
        if (objectClass.isArray()) {
            long size = UNSAFE.arrayBaseOffset(objectClass) + (long) UNSAFE.arrayIndexScale(objectClass) * Array.getLength(object);
            return padSize(size);
        } else {
            long size = OBJECT_BASE_SIZE;
            do {
                for (Field field : objectClass.getDeclaredFields()) {
                    if ((field.getModifiers() & Modifier.STATIC) == 0) {
                        long offset = UNSAFE.objectFieldOffset(field);
                        if (offset >= size) {
                            size = offset + 1;
                        }
                    }
                }
                objectClass = objectClass.getSuperclass();
            } while (objectClass != null);
            return padSize(size);
        }
    }

    private static long padSize(long size) {
        return (size + (OBJECT_ALIGNMENT - 1)) & -OBJECT_ALIGNMENT;
    }

    public static long deepSizeOf(final Object object) {
        IdentityHashMap<Object, Object> visited = new IdentityHashMap<>();
        Stack<Object> stack = new Stack<>();
        if (object != null) stack.push(object);

        long size = 0;
        while (!stack.isEmpty()) {
            size += internalSizeOf(stack.pop(), stack, visited);
        }
        return size;
    }

    private static long internalSizeOf(Object object, Stack<Object> stack, IdentityHashMap<Object, Object> visited) {
        // scan for object reference and add to stack
        Class<?> c = object.getClass();
        if (c.isArray() && !c.getComponentType().isPrimitive()) {
            // add unseen array elements to stack
            for (int i = Array.getLength(object) - 1; i >= 0; i--) {
                Object val = Array.get(object, i);
                if (val != null && visited.put(val, val) == null) {
                    stack.add(val);
                }
            }
        } else {
            // add unseen object reference to the stack
            for (; c != null; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if ((field.getModifiers() & Modifier.STATIC) == 0 && !field.getType().isPrimitive()) {
                        field.setAccessible(true);
                        try {
                            Object val = field.get(object);
                            if (val != null && visited.put(val, val) == null) {
                                stack.add(val);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            log.error("error in internalSizeOf {}  ", e.getMessage());
                        }
                    }
                }
            }
        }
        return shallowSizeOf(object);
    }
}
