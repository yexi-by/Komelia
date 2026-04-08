function(komelia_android_autotools_env output_var)
    if (NOT ANDROID)
        set(${output_var} "" PARENT_SCOPE)
        return()
    endif ()

    set(_sysroot "${CMAKE_BINARY_DIR}/sysroot")
    set(_cc "$ENV{CC}")
    if (NOT _cc)
        set(_cc "${CMAKE_C_COMPILER}")
    endif ()

    set(_cxx "$ENV{CXX}")
    if (NOT _cxx)
        set(_cxx "${CMAKE_CXX_COMPILER}")
    endif ()

    set(_ar "$ENV{AR}")
    if (NOT _ar)
        set(_ar "${CMAKE_AR}")
    endif ()

    set(_as "$ENV{AS}")
    if (NOT _as)
        set(_as "${_cc}")
    endif ()

    set(_ld "$ENV{LD}")
    if (NOT _ld)
        set(_ld "${CMAKE_LINKER}")
    endif ()

    set(_nm "$ENV{NM}")
    if (NOT _nm)
        set(_nm "${CMAKE_NM}")
    endif ()

    set(_ranlib "$ENV{RANLIB}")
    if (NOT _ranlib)
        set(_ranlib "${CMAKE_RANLIB}")
    endif ()

    set(_strip "$ENV{STRIP}")
    if (NOT _strip)
        set(_strip "${CMAKE_STRIP}")
    endif ()

    set(_env
            ${CMAKE_COMMAND} -E env
            "CC=${_cc}"
            "CXX=${_cxx}"
            "CPP=${_cc} -E"
            "AR=${_ar}"
            "AS=${_as}"
            "LD=${_ld}"
            "NM=${_nm}"
            "RANLIB=${_ranlib}"
            "STRIP=${_strip}"
            "CONFIG_SITE=/dev/null"
            "PKG_CONFIG_DIR=$ENV{PKG_CONFIG_DIR}"
            "PKG_CONFIG_LIBDIR=$ENV{PKG_CONFIG_LIBDIR}"
            "PKG_CONFIG_PATH=$ENV{PKG_CONFIG_PATH}"
            "CPPFLAGS=--sysroot=${CMAKE_SYSROOT} -I${_sysroot}/include"
            "CFLAGS=--sysroot=${CMAKE_SYSROOT} -fPIC -I${_sysroot}/include"
            "CXXFLAGS=--sysroot=${CMAKE_SYSROOT} -fPIC -I${_sysroot}/include"
            "LDFLAGS=--sysroot=${CMAKE_SYSROOT} -L${_sysroot}/lib"
    )
    set(${output_var} ${_env} PARENT_SCOPE)
endfunction()

function(komelia_android_autotools_shell_command output_var shell_command)
    komelia_android_autotools_env(_env)
    if (ANDROID)
        set(${output_var}
                ${_env}
                /bin/bash -lc
                "${shell_command}"
                PARENT_SCOPE
        )
        return()
    endif ()

    set(${output_var} "" PARENT_SCOPE)
endfunction()
