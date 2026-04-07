"""对 Android 构建用的 libvips 源码应用稳定补丁。"""

from __future__ import annotations

import sys
from pathlib import Path


def replace_once(content: str, old: str, new: str, file_path: Path) -> str:
    """执行一次精确替换，缺失时立即失败。"""

    if new in content:
        return content

    if old not in content:
        raise RuntimeError(f"无法在 {file_path} 中找到预期补丁上下文。")

    return content.replace(old, new, 1)


def patch_thumbnail(source_root: Path) -> None:
    """为 thumbnail 操作增加 kernel 参数支持。"""

    file_path = source_root / "libvips" / "resample" / "thumbnail.c"
    content = file_path.read_text(encoding="utf-8")

    content = replace_once(
        content,
        "gboolean page_pyramid;\n\n} VipsThumbnail;",
        "gboolean page_pyramid;\n\tVipsKernel kernel;\n\n} VipsThumbnail;",
        file_path,
    )
    content = replace_once(
        content,
        'if (vips_resize(in, &t[5], 1.0 / hshrink, "vscale", 1.0 / vshrink, NULL))',
        'if (vips_resize(in, &t[5], 1.0 / hshrink, "vscale", 1.0 / vshrink,\n\t\t\t"kernel", thumbnail->kernel, NULL))',
        file_path,
    )
    content = replace_once(
        content,
        '\tVIPS_ARG_STRING(class, "output_profile", 119,\n\t\t_("Output profile"),\n\t\t_("Fallback output profile"),\n\t\tVIPS_ARGUMENT_OPTIONAL_INPUT,\n\t\tG_STRUCT_OFFSET(VipsThumbnail, output_profile),\n\t\tNULL);\n',
        '\tVIPS_ARG_STRING(class, "output_profile", 119,\n\t\t_("Output profile"),\n\t\t_("Fallback output profile"),\n\t\tVIPS_ARGUMENT_OPTIONAL_INPUT,\n\t\tG_STRUCT_OFFSET(VipsThumbnail, output_profile),\n\t\tNULL);\n\n\tVIPS_ARG_ENUM(class, "kernel", 999,\n\t\t_("Kernel"),\n\t\t_("Resampling kernel"),\n\t\tVIPS_ARGUMENT_OPTIONAL_INPUT,\n\t\tG_STRUCT_OFFSET(VipsThumbnail, kernel),\n\t\tVIPS_TYPE_KERNEL, VIPS_KERNEL_LANCZOS3);\n',
        file_path,
    )
    content = replace_once(
        content,
        "\tthumbnail->auto_rotate = TRUE;\n\tthumbnail->intent = VIPS_INTENT_RELATIVE;\n\tthumbnail->fail_on = VIPS_FAIL_ON_NONE;\n",
        "\tthumbnail->auto_rotate = TRUE;\n\tthumbnail->intent = VIPS_INTENT_RELATIVE;\n\tthumbnail->kernel = VIPS_KERNEL_LANCZOS3;\n\tthumbnail->fail_on = VIPS_FAIL_ON_NONE;\n",
        file_path,
    )

    file_path.write_text(content, encoding="utf-8", newline="\n")


def patch_meson(source_root: Path) -> None:
    """放宽 libhwy 版本元数据校验，兼容 Android 交叉构建产物。"""

    file_path = source_root / "meson.build"
    content = file_path.read_text(encoding="utf-8")

    content = replace_once(
        content,
        "# Require 1.0.5 to support the `ReorderDemote2To(u8, i16, i16)` operation\n# See: https://github.com/google/highway/pull/1247\nlibhwy_dep = dependency('libhwy', version: '>=1.0.5', required: get_option('highway'))\n",
        "# Require 1.0.5 to support the `ReorderDemote2To(u8, i16, i16)` operation.\n# Android cross builds can produce a valid libhwy.pc without a Version field,\n# so we accept an empty version string and treat it as sufficiently new.\nlibhwy_dep = dependency('libhwy', required: get_option('highway'))\nlibhwy_version = libhwy_dep.version()\n",
        file_path,
    )
    content = replace_once(
        content,
        "    cfg_var.set('HAVE_HWY_1_1_0', libhwy_dep.version().version_compare('>=1.1.0'))\n",
        "    cfg_var.set('HAVE_HWY_1_1_0', libhwy_version == '' or libhwy_version.version_compare('>=1.1.0'))\n",
        file_path,
    )
    content = replace_once(
        content,
        "    if libhwy_dep.version().version_compare('>=1.3.0')\n",
        "    if libhwy_version == '' or libhwy_version.version_compare('>=1.3.0')\n",
        file_path,
    )

    file_path.write_text(content, encoding="utf-8", newline="\n")


def main(argv: list[str]) -> int:
    """执行 libvips Android 构建所需的补丁。"""

    if len(argv) != 2:
        raise SystemExit("用法: apply_vips_patch.py <vips-source-dir>")

    source_root = Path(argv[1]).resolve()
    patch_thumbnail(source_root)
    patch_meson(source_root)
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv))
