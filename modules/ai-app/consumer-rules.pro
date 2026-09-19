# AI短剧工厂混淆规则（随宿主 app:release 的 minify 生效）
# ffmpeg-kit 反射调用 + kotlinx.serialization 注解处理器产物必须保留
-keep class com.dramafactory.** { *; }
-keep class com.arthenica.** { *; }
-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod