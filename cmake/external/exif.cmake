include(ExternalProject)
include(${CMAKE_CURRENT_LIST_DIR}/android-autotools.cmake)

if (ANDROID)
    komelia_android_autotools_shell_command(
            EXIF_CONFIGURE_COMMAND
            "cd <SOURCE_DIR> && ./configure ${HOST_FLAG} --prefix ${CMAKE_BINARY_DIR}/sysroot"
    )
else ()
    set(EXIF_CONFIGURE_COMMAND
            cd <SOURCE_DIR> && ./configure ${HOST_FLAG} --prefix ${CMAKE_BINARY_DIR}/sysroot
    )
endif ()

ExternalProject_Add(ep_exif
        URL https://github.com/libexif/libexif/releases/download/v0.6.25/libexif-0.6.25.tar.xz
        DEPENDS ep_zlib
        CONFIGURE_COMMAND ${EXIF_CONFIGURE_COMMAND}
        BUILD_COMMAND ${Make_EXECUTABLE} all
        INSTALL_COMMAND ${Make_EXECUTABLE} install
        BUILD_IN_SOURCE true
        USES_TERMINAL_DOWNLOAD true
        USES_TERMINAL_BUILD true
)
