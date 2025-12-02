package io.github.andannn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base class for retained model.
 */
abstract class RetainedModel {
    /**
     * Scope for retained model which will be canceled when this model is retired.
     */
    val retainedScope: CoroutineScope = createViewModelScope()

    internal fun clear() {
        retainedScope.cancel()
        onClear()
    }

    /**
     * Called when this model is retired.
     */
    open fun onClear() {}
}

/**
 * retain a [RetainedModel].
 */
@Composable
fun <T : RetainedModel> retainRetainedModel(
    vararg keys: Any?,
    factory: () -> T,
): T =
    retain(keys = keys) {
        RetainedModelObserver(factory())
    }.retainedModel

private class RetainedModelObserver<T : RetainedModel>(
    val retainedModel: T,
) : RetainObserver {
    override fun onRetained() {}

    override fun onEnteredComposition() {}

    override fun onExitedComposition() {}

    override fun onRetired() {
        retainedModel.clear()
    }

    override fun onUnused() {
        retainedModel.clear()
    }
}

internal fun createViewModelScope(): CoroutineScope {
    val dispatcher =
        try {
            Dispatchers.Main.immediate
        } catch (_: NotImplementedError) {
            EmptyCoroutineContext
        } catch (_: IllegalStateException) {
            EmptyCoroutineContext
        }
    return CoroutineScope(context = dispatcher + SupervisorJob())
}
