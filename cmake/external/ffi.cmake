include(ExternalProject)
include(${CMAKE_CURRENT_LIST_DIR}/android-autotools.cmake)

if (ANDROID)
    komelia_android_autotools_shell_command(
            FFI_CONFIGURE_COMMAND
            "cd <SOURCE_DIR> && ./autogen.sh && ./configure ${HOST_FLAG} --disable-builddir --disable-exec-static-tramp --disable-multi-os-directory --disable-static --enable-pax_emutramp --prefix ${CMAKE_BINARY_DIR}/sysroot"
    )
else ()
    set(FFI_CONFIGURE_COMMAND
            <SOURCE_DIR>/autogen.sh && <SOURCE_DIR>/configure ${HOST_FLAG}
            --disable-builddir
            --disable-exec-static-tramp
            --disable-multi-os-directory
            --disable-static --enable-pax_emutramp
            --prefix ${CMAKE_BINARY_DIR}/sysroot
    )
endif ()

ExternalProject_Add(ep_ffi
        GIT_REPOSITORY https://github.com/libffi/libffi
        GIT_TAG v3.5.2
        GIT_SHALLOW 1
        GIT_PROGRESS 1
        BUILD_IN_SOURCE 1
        CONFIGURE_COMMAND ${FFI_CONFIGURE_COMMAND}
        BUILD_COMMAND ${Make_EXECUTABLE}
        INSTALL_COMMAND ${Make_EXECUTABLE} install
        USES_TERMINAL_DOWNLOAD true
        USES_TERMINAL_BUILD true
)
