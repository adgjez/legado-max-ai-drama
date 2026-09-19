package com.dramafactory.app

import android.app.Application
import android.util.Log
import com.dramafactory.app.ui.RenderRuntime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * AI短剧工厂初始化入口：由宿主 App.onCreate 异步调用（替代原独立 DramaApplication）。
 *
 * 移植自 ai-drama-factory DramaApplication.onCreate：
 * - installCrashLogger 最先安装（未捕获异常写 files/crash/last_crash.txt）；
 * - AppGraph.init 内部各步骤独立容错，本函数再包一层 runCatching 双保险；
 * - recoverOnBoot 已包 runCatching，绝不阻断宿主启动。
 */
object AiAppBootstrap {

    /** App 级作用域：SupervisorJob 保证子协程异常不互相拖垮 */
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun init(app: Application) {
        CrashLog.installCrashLogger(app)
        runCatching { AppGraph.init(app) }
            .onFailure { Log.e("DramaApp", "AppGraph.init failed", it) }
        // 队列协程作用域接线（DefaultRenderQueue worker 在此 scope 内运行）
        runCatching { RenderRuntime.bindScope(appScope) }
        // P1-6 进程重启恢复：读 checkpoint → repoll 已提交镜 → 续跑队列（绝不重复扣费）
        appScope.launch { runCatching { RenderRuntime.recoverOnBoot() } }
    }
}