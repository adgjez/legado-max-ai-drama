package com.dramafactory.app

import androidx.core.content.FileProvider

/**
 * AI短剧工厂专用 FileProvider 子类。
 *
 * 宿主（app 模块）已声明 androidx.core.content.FileProvider（authority=${applicationId}.fileProvider），
 * 若 library 再声明同名 class 会触发 manifest merger 冲突；以子类身份注册即可各自独立，
 * authority 保持固定的 com.dramafactory.app.fileprovider（代码侧全部引用该串）。
 */
class DramaFileProvider : FileProvider()