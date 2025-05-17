package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.Ad
import kotlinx.coroutines.flow.Flow
import com.oneplus.redcableclub.R
import kotlinx.coroutines.flow.flowOf

class FakeAdRepository: AdRepository {

    val ads = listOf<Ad>(
        Ad("Save €100 (16GB only), plus get 2 free gifts worth up to €104",R.drawable.oneplus_13_ad),
        Ad("Save €50, plus get a free gift worth up to €49",R.drawable.oneplus_13r_ad),
        Ad("Save €50, plus get FREE earbuds worth up to €79 (Limited stock)",R.drawable.oneplus_watch_3_ad)
    )

    val discoverPosts = listOf<Ad>(
        Ad("Shot on OnePlus: Faces", R.drawable.faces),
        Ad("Flex your Flux", R.drawable.flux),
        Ad("Plus Key", R.drawable.plus)
    )



    override fun getAds(): Flow<List<Ad>> {
        return flowOf(ads)
    }

    override fun getDiscoverPosts(): Flow<List<Ad>> {
        TODO("Not yet implemented")
    }


}