package com.tmdb.ui.details.person

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tmdb.R
import com.tmdb.helpers.FormatHelper
import com.tmdb.helpers.ImageHelper
import com.tmdb.models.details.person.PersonDetails
import com.tmdb.models.details.person.images.PersonImages
import com.tmdb.models.details.person.movie.credits.PersonMovieCredits
import com.tmdb.models.details.person.tvshow.credits.PersonTvShowCredits
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.person_details_activity.*
import kotlinx.android.synthetic.main.person_details_facts.*
import java.util.*

class PersonDetailsActivity : MvpAppCompatActivity(), PersonDetailsView {

    @InjectPresenter
    lateinit var presenter: PersonDetailsPresenter

    companion object {
        const val PERSON_ID_EXTRA = "person_id"
        const val IMAGE_CONFIGURATION_EXTRA = "image_configuration"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.person_details_activity)
        setSupportActionBar(person_details_toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    override fun getExtra() {
        presenter.person_id = intent.getIntExtra(PERSON_ID_EXTRA, 1)
        presenter.imageHelper = ImageHelper(intent.getParcelableExtra(IMAGE_CONFIGURATION_EXTRA))
    }

    override fun showPersonDetails(person: PersonDetails,
                                   movieCredits: PersonMovieCredits,
                                   tvShowCredits: PersonTvShowCredits,
                                   images: PersonImages) {

        displayTitle(person.name)
        displayImages(person.profilePath, movieCredits, tvShowCredits)
        displayBiography(person.biography)
        displayFacts(person)

    }

    private fun displayTitle(name: String) {
        person_details_name.text = name

        person_details_app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                scrollRange = appBarLayout.totalScrollRange

                if (scrollRange + verticalOffset == 0) {
                    person_details_collapsing_layout.title = name
                    isShow = true
                } else if (isShow) {
                    person_details_collapsing_layout.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun displayImages(profilePath: String?, movies: PersonMovieCredits, tvShows: PersonTvShowCredits) {

        //Backdrop Image
        val backdropPath = ImageHelper.getBackdropPathForPersonDetails(profilePath, movies, tvShows)
        Glide.with(applicationContext)
            .load(presenter.imageHelper!!.getBackdropUrl(backdropPath))
            .error(R.drawable.random_backdrop)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(person_details_title_background_img)

        //Profile Image
        Glide.with(applicationContext)
            .load(presenter.imageHelper!!.getProfileUrl(profilePath))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.random_face)
            .into(person_details_profile_img)

    }

    private fun displayBiography(biography: String) {
        if (biography.isNotEmpty()) {
            person_details_biography_text.text = biography
        } else {
            person_details_container_biography.visibility = View.GONE
        }
    }

    private fun displayFacts(person: PersonDetails) = with(person) {

        //Birthday
        if (birthday != null && birthday.isNotEmpty())
            person_details_date_of_birth_text.text = birthday
        else
            person_details_facts_item_container_birth_date.visibility = View.GONE

        //Day of Death
        if (deathday != null && deathday.isNotEmpty())
            person_details_date_of_death_text.text = deathday
        else
            person_details_facts_item_container_death_date.visibility = View.GONE

        //Birth place
        if (placeOfBirth != null && placeOfBirth.isNotEmpty())
            person_details_birth_place_text.text = placeOfBirth
        else
            person_details_facts_item_container_birth_place.visibility = View.GONE

        //Age
        if (birthday != null && birthday.isNotEmpty() && (deathday == null || deathday.isEmpty())) {
            val age = FormatHelper.getAge(birthday, Calendar.getInstance().get(Calendar.YEAR))
            person_details_age_text.text = age
        } else {
            person_details_facts_item_container_age.visibility = View.GONE
        }

        //Also known as
        if (alsoKnownAs.isNotEmpty())
            person_details_also_known_as_text.text = FormatHelper.alsoKnownAsToString(alsoKnownAs)
        else
            person_details_facts_item_container_known_as.visibility = View.GONE

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.compositeDisposable.dispose()
        presenter.compositeDisposable.clear()
        presenter.compositeDisposable = CompositeDisposable()
    }
}