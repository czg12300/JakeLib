
package com.jake.library.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jake.library.R;
import com.jake.library.ui.adapter.BaseRecycleViewAdapter;
import com.jake.library.ui.fragment.BaseFragment;
import com.jake.library.ui.widget.BaseLoadStateLayoutView;
import com.jake.library.ui.widget.irecyclerview.IRecyclerView;
import com.jake.library.ui.widget.irecyclerview.LoadMoreFooterView;
import com.jake.library.ui.widget.irecyclerview.OnLoadMoreListener;
import com.jake.library.ui.widget.irecyclerview.OnRefreshListener;

import java.util.List;

/**
 * 描述:主要用于有下拉刷新和上拉加载的页面
 *
 * @author jakechen
 * @since 2016/8/19 15:26
 */
public abstract class BaseRefreshListFragment<AdapterEntity extends BaseRecycleViewAdapter>
        extends BaseFragment {
    protected IRecyclerView mRecyclerView;

    private LoadMoreFooterView mLoadMoreFooterView;

    protected AdapterEntity mAdapter;

    private static final int PAGE_SIZE = 10;

    private static final int PAGE_START = 1;

    private int pageIndex = PAGE_START;

    private int pageSize = PAGE_SIZE;

    private int pageStart = PAGE_START;

    private BaseLoadStateLayoutView mStateLayoutView;
    /**
     * 由于部分页面可能使用懒加载数据的方法，用这个参数了标示是否执行过第一次数据加载
     */
    private boolean mIsFinishFirstLoadData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = createContentView();
        if (rootView != null) {
            mStateLayoutView = findLoadStateLayoutView(rootView);
            if (mStateLayoutView == null) {
                throw new NullPointerException("you must override findLoadStateLayoutView");
            }
        } else {
            mStateLayoutView = createLoadStateLayoutView(getActivity());
            mStateLayoutView.setContentView(R.layout.fragment_base_refresh_list);
            mStateLayoutView.setOnRefreshButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStateLayoutView.showLoadingView();
                    loadMore(pageIndex, pageSize);
                }
            });
            rootView = mStateLayoutView;
        }
        return rootView;
    }

    /**
     * 创建LoadStateLayoutView
     *
     * @param activity
     * @return
     */
    protected abstract BaseLoadStateLayoutView createLoadStateLayoutView(FragmentActivity activity);

    /**
     * 创建一个含有LoadStateLayoutView的content view
     *
     * @return
     */
    protected View createContentView() {
        return null;
    }

    /**
     * 找到LoadStateLayoutView
     *
     * @param rootView
     * @return
     */
    protected BaseLoadStateLayoutView findLoadStateLayoutView(View rootView) {
        return null;
    }

    /**
     * 设置每页数据多少，默认10条
     *
     * @param pageSize
     */
    protected void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 初始化recycle view，主要是设置layoutManager和设置footer、header和addItemDecoration
     *
     * @param iRecyclerView
     */
    protected abstract void initRecycleView(IRecyclerView iRecyclerView);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (IRecyclerView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        initRecycleView(mRecyclerView);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mLoadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                loadMore(pageIndex, pageSize);
            }

        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View loadMoreView) {
                if (mLoadMoreFooterView.canLoadMore()
                        && mRecyclerView.getIAdapter().getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
                    loadMore(pageIndex, pageSize);
                }
            }
        });
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mRecyclerView.getRefreshEnabled()) {
                    mRecyclerView.setRefreshing(true);
                }
                pageIndex = pageStart;
                refresh(pageIndex, pageSize);
            }
        });
        mAdapter = createAdapter();
        mRecyclerView.setIAdapter(mAdapter);
        initView();
        mStateLayoutView.showLoadingView();
        firstLoadData();
    }

    /**
     * 第一次加载数据
     */
    protected void firstLoadData() {
        mStateLayoutView.showLoadingView();
        loadMore(pageIndex, pageSize);
        invokedFirstLoadData();
    }

    /**
     * 执行过第一次加载数据方法
     */
    protected void invokedFirstLoadData() {
        mIsFinishFirstLoadData = true;
    }

    /**
     * 是否开始第一次加载数据
     *
     * @return
     */
    protected boolean isStartFirstLoadData() {
        return mIsFinishFirstLoadData;
    }

    /**
     * 如果需要，可以在这里初始化一下东西
     */
    protected void initView() {

    }

    public IRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 创建adapter
     *
     * @return
     */
    protected abstract AdapterEntity createAdapter();

    /**
     * 获取当前的adapter，mAdapter的作用于为protected的，子类可以直接使用
     *
     * @return
     */
    public AdapterEntity getAdapter() {
        return mAdapter;
    }

    /**
     * 如果需要刷新功能可以在重写本方法，增加刷新业务
     *
     * @param pageStart
     * @param pageSize
     */
    protected void refresh(int pageStart, int pageSize) {
    }

    /**
     * 设置分页加载的起始页码
     *
     * @param pageStart
     */
    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    /**
     * 设置当前页码
     *
     * @param pageIndex
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 获取每页的数据量
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 用于判断当前页面是否为第一页
     *
     * @return
     */
    public boolean isFirstPage() {
        return pageIndex == pageStart;
    }

    /**
     * 获取其实页码
     *
     * @return
     */
    public int getPageStart() {
        return pageStart;
    }

    /**
     * 加载下一页数据，子类必须实现这个逻辑
     *
     * @param pageIndex
     * @param pageSize
     */
    protected abstract void loadMore(int pageIndex, int pageSize);

    /**
     * 完成下拉刷新后调用
     */
    protected void finishRefresh() {
        mRecyclerView.setRefreshing(false);
    }

    protected void showRefreshView() {
        mRecyclerView.setRefreshing(true);
    }

    /**
     * 设置是否显示顶部阴影
     *
     * @param isShow
     */
    protected void showShadow(boolean isShow) {
        findViewById(R.id.v_shadow_top).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 提供加载状态的view，可通过这个view设置显示的状态
     *
     * @return
     */
    protected BaseLoadStateLayoutView getLoadStateLayoutView() {
        return mStateLayoutView;
    }

    /**
     * 下一页，数据加载完成后，需要加载下一页的时候调用
     */
    protected void nextPage() {
        pageIndex++;
    }

    /**
     * 完成上拉加载后调用
     *
     * @param hasNext
     */
    protected void finishLoadMore(boolean hasNext) {
        finishLoadMore(hasNext, true);
    }

    protected void finishLoadMore(boolean hasNext, boolean needShowFinishState) {
        if (!hasNext) {
            if (needShowFinishState) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            } else {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }
            mRecyclerView.setLoadMoreEnabled(false);
        } else {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            mRecyclerView.setLoadMoreEnabled(true);
        }
    }

    /**
     * 隐藏加载完毕
     */
    protected void hideLoadMoreFinishState() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    /**
     * 处理加载数据成功后的ui显示的事情,此方法调用必须在add data之前
     *
     * @param dataSize
     * @param hasNext
     */
    protected void handleLoadDataSuccess(int dataSize, boolean hasNext) {
        handleLoadDataSuccess(dataSize, hasNext, true);
    }


    protected void handleLoadDataSuccess(int dataSize, boolean hasNext, boolean needShowState) {
        if (isFirstPage()) {
            if (getAdapter().getItemCount() == 0 || needShowState) {
                if (dataSize > 0) {
                    getLoadStateLayoutView().showLoadSuccessView();
                } else {
                    getLoadStateLayoutView().showNoDataView();
                }
            }
            //清除数据
            mAdapter.clear();
        }
        handleRefreshAndLoadMoreState(dataSize, hasNext, needShowState);
    }

    /**
     * 处理刷新状态
     *
     * @param dataSize
     * @param hasNext
     * @param needShowState
     */
    public void handleRefreshAndLoadMoreState(int dataSize, boolean hasNext, boolean needShowState) {
        finishRefresh();
        finishLoadMore(hasNext, dataSize == 0 && mAdapter.getItemCount() == 0 ? false : needShowState);
        nextPage();
    }

    /**
     * 判断列表是否可用，判断标准为不为空并且size>0
     *
     * @param list
     * @return
     */
    protected boolean isListAvailable(List<?> list) {
        return list != null && list.size() > 0;
    }

    /**
     * 设置第一条数据里顶部的距离
     *
     * @param paddingTop
     */

    protected void setFirstItemPaddingTop(float paddingTop) {
        mRecyclerView.addItemDecoration(new SpaceListItemDecoration(paddingTop));
    }

    /**
     * 提供list的分割线，第一条数据距离顶部的距离
     */
    public class SpaceListItemDecoration extends RecyclerView.ItemDecoration {
        private int paddingTop;

        public SpaceListItemDecoration(float paddingTop) {
            this.paddingTop = (int) paddingTop;
        }

        public SpaceListItemDecoration() {
            this(0);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            int position = parent.getChildLayoutPosition(view);
            outRect.left = 0;
            if (position > 0) {
                outRect.top = 0;
            } else {
                outRect.top = paddingTop > 2 ? paddingTop / 2 : 1;
            }
        }

    }
}
