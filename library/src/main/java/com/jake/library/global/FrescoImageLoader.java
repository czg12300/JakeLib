package com.jake.library.global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jake.library.utils.FileUtils;
import com.jake.library.utils.MD5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrescoImageLoader {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    private static final int MAX_DISK_CACHE_SIZE = 200 * ByteConstants.MB;
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    private static final String IMAGE_PIPELINE_CACHE_DIR = ".imageCache";

    private static ImagePipelineConfig.Builder builder;

    public final static void init(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        builder = ImagePipelineConfig.newBuilder(appContext).setDownsampleEnabled(true).setBitmapsConfig(Bitmap.Config.RGB_565);
        builder = configureCaches(builder, appContext);
        Fresco.initialize(appContext, builder.build());
    }

    private static ImagePipelineConfig.Builder configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                FrescoImageLoader.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                FrescoImageLoader.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        File cache = context.getExternalCacheDir();
        if (cache == null)
            cache = Environment.getExternalStorageDirectory();
        configBuilder.setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
//                                .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                                .setBaseDirectoryPath(cache)
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                .setMaxCacheSize(FrescoImageLoader.MAX_DISK_CACHE_SIZE)
                                .build());
        return configBuilder;
    }

    public static void preloadIcon(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .build();
        imagePipeline.prefetchToDiskCache(imageRequest, null);
    }

    public static void load(Uri picUri, DraweeView draweeView, ControllerListener listener, boolean needShowGif) {
        if (picUri == null || picUri.compareTo(Uri.EMPTY) == 0 || draweeView == null) {
            return;
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(picUri).setAutoPlayAnimations(needShowGif)
                .setTapToRetryEnabled(true).setOldController(draweeView.getController()).setControllerListener(listener).build();
        draweeView.setController(controller);
    }

    public static void load(String url, DraweeView draweeView, ControllerListener listener, boolean needShowGif) {
        load(parseUrl(url), draweeView, listener, needShowGif);
    }

    public static void load(String url, DraweeView draweeView, ControllerListener listener) {
        load(url, draweeView, listener, false);
    }

    public static void load(Uri picUri, DraweeView draweeView, boolean needShowGif) {
        load(picUri, draweeView, null, needShowGif);
    }

    public static void load(Uri picUri, DraweeView draweeView) {
        load(picUri, draweeView, false);
    }

    public static void load(String url, DraweeView view) {
        load(url, view, false);
    }

    public static void load(String url, DraweeView view, boolean needShowGif) {
        load(parseUrl(url), view, needShowGif);
    }

    private static Uri parseUrl(String url) {
        return TextUtils.isEmpty(url) ? null : Uri.parse(url);
    }

    public static void loadWithWrapContent(String url, DraweeView draweeView) {
        loadWithWrapContent(url, draweeView, false);
    }

    public static void loadWithWrapContent(Uri uri, DraweeView draweeView) {
        loadWithWrapContent(uri, draweeView, false);
    }

    public static void loadWithWrapContent(String url, final DraweeView draweeView, boolean needShowGif) {
        loadWithWrapContent(parseUrl(url), draweeView, needShowGif);
    }

    public static void loadWithWrapContent(Uri uri, final DraweeView draweeView, boolean needShowGif) {
        ControllerListener listener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                updateDraweeViewSize(draweeView, imageInfo);
            }


            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                updateDraweeViewSize(draweeView, imageInfo);
            }
        };
        load(uri, draweeView, listener, needShowGif);
    }

    /**
     * 根据ImageInfo来改变DraweeView的大小
     *
     * @param draweeView
     * @param imageInfo
     */
    public static void updateDraweeViewSize(DraweeView draweeView, ImageInfo imageInfo) {
        if (imageInfo != null && draweeView != null) {
            int screenWidth = draweeView.getResources().getDisplayMetrics().widthPixels;
            int width = imageInfo.getWidth();
            if (width > screenWidth) {
                width = screenWidth;
            }
            int layoutWidth = draweeView.getLayoutParams().width;
            if (layoutWidth <= 0 && layoutWidth != ViewGroup.LayoutParams.MATCH_PARENT) {
                draweeView.getLayoutParams().width = width;
            }
            draweeView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            float aspectRatio = (float) imageInfo.getWidth() / imageInfo.getHeight();
            draweeView.setAspectRatio(aspectRatio);
        }
    }

    public static void updateDraweeViewSize(DraweeView draweeView, ImageInfo imageInfo, float minAspectRatio) {
        if (imageInfo != null && draweeView != null) {
            int screenWidth = draweeView.getResources().getDisplayMetrics().widthPixels;
            int width = imageInfo.getWidth();
            if (width > screenWidth) {
                width = screenWidth;
            }
            int layoutWidth = draweeView.getLayoutParams().width;
            if (layoutWidth <= 0 && layoutWidth != ViewGroup.LayoutParams.MATCH_PARENT) {
                draweeView.getLayoutParams().width = width;
            }
            draweeView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            float aspectRatio = (float) imageInfo.getWidth() / imageInfo.getHeight();
            if (aspectRatio < minAspectRatio) {
                aspectRatio = minAspectRatio;
            }
            draweeView.setAspectRatio(aspectRatio);
        }
    }

    public static void loadRes(int drawableID, DraweeView view) {
        loadRes(drawableID, view, false);
    }

    public static void loadRes(int drawableID, DraweeView view, boolean needShowGif) {
        load(parseUrl("res://" + view.getContext().getPackageName() + "/" + drawableID), view, needShowGif);
    }

    public static void loadAsset(String filePath, DraweeView view) {
        loadAsset(filePath, view, false);
    }

    public static void loadAsset(String filePath, DraweeView view, boolean needShowGif) {
        load(parseUrl("asset:///" + filePath), view, needShowGif);
    }

    public static boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), "");
        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey);
    }

    //return file or null
    public static File getCachedImageOnDisk(Uri loadUri) {
        File localFile = null;
        try {
            if (loadUri != null) {
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), "");
                if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                    BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                    localFile = ((FileBinaryResource) resource).getFile();
                } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                    BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                    localFile = ((FileBinaryResource) resource).getFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localFile;
    }


    /**
     * 获取disk的缓存图片
     *
     * @param cacheKey
     * @return
     */
    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 获取默认的缓存key，通过Uri
     *
     * @param uri
     * @return
     */
    public static CacheKey getDefaultCacheKeyByUri(Uri uri) {
        return DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(uri), null);
    }

    /**
     * 获取已下载的图片
     *
     * @param url         图片下载链接
     * @param storagePath 图片保存地址
     * @return
     */
    public static File getDownloadImage(String url, String storagePath) {
        File localFile = null;
        if (!TextUtils.isEmpty(url)) {
            File file = new File(storagePath, getFileNameByUrl(url));
            if (file.exists() && file.isFile()) {
                localFile = file;
            }
        }
        return localFile;
    }

    /**
     * 使用fresco下载图片,先检查是否已经下载过，如果没有继续看是否用fresco缓存，如果还是没有就从网络下载
     *
     * @param url
     * @param storagePath
     * @param listener
     */
    public static void downloadImageByFresco(final String url, final String storagePath, final ImageLoaderListener listener) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(storagePath)) {
            if (listener != null) {
                listener.onLoadFailed();
            }
            return;
        }
        //判断是否已经下载过该图片
        File localFile = getDownloadImage(url, storagePath);
        if (localFile != null) {
            if (listener != null) {
                listener.onLoaded(Uri.parse(localFile.getAbsolutePath()));
            }
        } else {
            //判断是否用fresco缓存过该图片
            final File image = getCachedImageOnDisk(getDefaultCacheKeyByUri(Uri.parse(url)));
            if (image != null) {
                CallerThreadExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        String newFileName = getFileNameByUrl(url);
                        String resultFilePath = storagePath + File.separator + newFileName;
                        boolean isSuccess = FileUtils.copyFile(image.getAbsolutePath(), resultFilePath);
                        if (isSuccess) {
                            if (listener != null) {
                                listener.onLoaded(Uri.parse(resultFilePath));
                            }
                        } else {
                            if (listener != null) {
                                listener.onLoadFailed();
                            }
                        }
                    }
                });
            } else {
                //从网络下载图片
                downloadImageByFrescoWithNetwork(url, storagePath, listener);
            }
        }


    }

    /**
     * 通过fresco 从网络下载图片【
     *
     * @param url
     * @param storagePath
     * @param listener
     */
    public static void downloadImageByFrescoWithNetwork(final String url, final String storagePath, final ImageLoaderListener listener) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(storagePath)) {
            if (listener != null) {
                listener.onLoadFailed();
            }
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (listener != null) {
                    listener.onLoadFailed();
                }
            }

            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    if (listener != null) {
                        listener.onLoadFailed();
                    }
                }
                File appDir = new File(storagePath);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = getFileNameByUrl(url);
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    if (listener != null) {
                        listener.onLoaded(Uri.parse(file.getAbsolutePath()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onLoadFailed();
                    }
                }
            }
        }, CallerThreadExecutor.getInstance());
    }

    @NonNull
    private static String getFileNameByUrl(String url) {
        String newFileName = MD5Utils.getMd5(url) + ".jpg";
        if (url.startsWith("http://") || url.startsWith("https://")) {
            newFileName = url.substring(url.lastIndexOf("/"));
        }
        return newFileName;
    }

    public interface ImageLoaderListener {
        void onLoaded(Uri imgUri);

        void onLoadFailed();
    }

    public static void load(Uri uri, DraweeView view, int width, int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(request)
                .build();
        view.setController(controller);
    }
}