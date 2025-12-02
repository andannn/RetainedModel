A small helper for build retained model similar to android ViewModel.

### Install

libs.versions.toml

```toml
[versions]
retainedmodel = "1.0.0"

[dependencies]
retainedmodel = { group = "io.github.andannn", name = "retainedmodel", version.ref = "retainedmodel" }
```

then

```
dependencies {
    implementation(libs.retainedmodel)
}
```

### Quick Start

```kotlin
@Composable
fun retainedCounterModel(): CounterModel =
    retainRetainedModel {
        CounterModel()
    }

class CounterModel : RetainedModel() {
    init {
        retainedScope.launch {
            // launch coroutine scoped by retainedScope which will be canceled when this model is retired.
        }
    }

    override fun onClear() {
        super.onClear()
        // clear your resource..
    }
}
```