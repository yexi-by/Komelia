include(ExternalProject)
include(${CMAKE_CURRENT_LIST_DIR}/android-autotools.cmake)

if (ANDROID)
    komelia_android_autotools_shell_command(
            ICONV_CONFIGURE_COMMAND
            "cd <SOURCE_DIR> && ./configure ${HOST_FLAG} --prefix ${CMAKE_BINARY_DIR}/sysroot --enable-extra-encodings"
    )
else ()
    set(ICONV_CONFIGURE_COMMAND
            <SOURCE_DIR>/configure ${HOST_FLAG} --prefix ${CMAKE_BINARY_DIR}/sysroot
            --enable-extra-encodings
    )
endif ()

ExternalProject_Add(ep_iconv
        URL https://ftpmirror.gnu.org/libiconv/libiconv-1.18.tar.gz
        CONFIGURE_COMMAND ${ICONV_CONFIGURE_COMMAND}
        BUILD_COMMAND ${Make_EXECUTABLE}
        INSTALL_COMMAND ${Make_EXECUTABLE} install
        BUILD_IN_SOURCE true
        USES_TERMINAL_DOWNLOAD true
        USES_TERMINAL_BUILD true
)
