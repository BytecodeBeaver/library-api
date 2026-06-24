package com.github.bytecodebeaver.libraryapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * This configuration class enables DTO serialization for PageImpl objects, in a way that prevents exposing
 * Implementation details.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class PaginationDTOConfig {
}
