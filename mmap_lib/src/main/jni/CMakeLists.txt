cmake_minimum_required(VERSION 3.4.1)

add_library(mmap_logger SHARED mmap_logger.cpp)

find_library(log-lib log)

target_link_libraries(mmap_logger ${log-lib})