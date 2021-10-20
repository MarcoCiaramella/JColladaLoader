# JColladaLoader
[![](https://jitpack.io/v/MarcoCiaramella/JColladaLoader.svg)](https://jitpack.io/#MarcoCiaramella/JColladaLoader)

A Collada file loader library for Android. The code is part of [The 3Deers repo](https://github.com/the3deers/android-3D-model-viewer)

## How to import in your Android project
Add JitPack in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency
```
dependencies {
	        implementation 'com.github.MarcoCiaramella:JColladaLoader:x.x.x'
	}
```

## How to use
```java
ColladaLoader colladaLoader = new ColladaLoader();
// filename.dae must be placed in the asset folder
List<Object3DData> object3DDataList = colladaLoader.load(context, "filename.dae");
```