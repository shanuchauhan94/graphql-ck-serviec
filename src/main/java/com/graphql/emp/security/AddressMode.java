package com.graphql.emp.security;

public enum AddressMode {

    UNKNOWN,
    MEM_32BIT, // 32-bit address mode using 32-bit reference
    MEM_64BIT, // 64-bit address mode using 32-bit reference
    MEM_64BIT_COMPRESSED_OOPS
}
