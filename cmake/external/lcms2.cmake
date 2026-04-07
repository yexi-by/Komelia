include(ExternalProject)
include(${CMAKE_CURRENT_LIST_DIR}/android-autotools.cmake)

if (ANDROID)
    komelia_android_autotools_shell_command(
            LCMS2_CONFIGURE_COMMAND
            "cd <SOURCE_DIR> && ./configure ${HOST_FLAG} --prefix ${CMAKE_BINARY_DIR}/sysroot"
    )
else ()
    set(LCMS2_CONFIGURE_COMMAND
            <SOURCE_DIR>/configure ${HOST_FLAG}
            --prefix ${CMAKE_BINARY_DIR}/sysroot
    )
endif ()

ExternalProject_Add(ep_lcms2
        GIT_REPOSITORY https://github.com/mm2/Little-CMS
        GIT_TAG lcms2.17
        GIT_SHALLOW 1
        GIT_PROGRESS 1
        DEPENDS ep_zlib
        CONFIGURE_COMMAND ${LCMS2_CONFIGURE_COMMAND}
        BUILD_COMMAND ${Make_EXECUTABLE}
        INSTALL_COMMAND ${Make_EXECUTABLE} install
        BUILD_IN_SOURCE true
        USES_TERMINAL_DOWNLOAD true
        USES_TERMINAL_BUILD true
)
