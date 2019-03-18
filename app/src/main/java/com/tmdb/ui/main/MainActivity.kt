package com.tmdb.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.tmdb.TMDbApplication
import com.tmdb.network.TMDbAPI
import javax.inject.Inject
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.tmdb.R
import com.tmdb.models.configuration.ImageConfiguration
import com.tmdb.models.genres.Genre
import com.tmdb.ui.adapters.ViewPagerAdapter
import com.tmdb.ui.main.fragments.discover.DiscoverFragment
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.navigation_drawer_main.*


class MainActivity : MvpAppCompatActivity(), MainView {
       // NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var TMDbAPI: TMDbAPI

    var viewPagerAdapter: ViewPagerAdapter? = null

    init {
        TMDbApplication.graph.inject(this)
    }

    private var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_main)
        setSupportActionBar(main_toolbar)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setHomeButtonEnabled(true)

    }

    override fun initDiscoverViewPager(configuration: ImageConfiguration,
                                       tvShowGenres: List<Genre>,
                                       movieGenres: List<Genre>) = with(main_view_pager) {
        main_tab_layout.setupWithViewPager(this)

        if (viewPagerAdapter == null) {
            Log.d("TAAAG", " 0")
            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
            viewPagerAdapter!!.addFragment(
                DiscoverFragment.getMovieDiscoverFragment(configuration, ArrayList(movieGenres)),
                resources.getString(R.string.discover_movies_title)
            )
            viewPagerAdapter!!.addFragment(
                DiscoverFragment.getTvShowDiscoverFragment(configuration, ArrayList(tvShowGenres)),
                resources.getString(R.string.discover_tv_shows_title)
            )
        }

        adapter = viewPagerAdapter
    }

    override fun showViewPager() {
        main_view_pager.visibility = View.VISIBLE
    }

    override fun hideViewPager() {
        main_view_pager.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        presenter.disposalble?.dispose()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle!!.onConfigurationChanged(newConfig)
    }

    //    override fun initNavigationDrawer() {
//        toggle = ActionBarDrawerToggle(
//            this,
//            navigation_drawer_layout,
//            main_toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        navigation_drawer_layout.addDrawerListener(toggle!!)
//        toggle!!.syncState()
//
//        navigation_view.setNavigationItemSelectedListener(this)
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        when(item.itemId) {
//            R.id.navigation_discover -> {
//
//            }
//            R.id.navigation_favorite -> {
//
//            }
//        }
//
//        navigation_drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }

    override fun onBackPressed() {
//        if (navigation_drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            navigation_drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
        super.onBackPressed()
    }
}