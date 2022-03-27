package com.haberturm.hitchhikingapp.ui.util

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel

class AssertParamType : NavType<GeocodeUiModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): GeocodeUiModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): GeocodeUiModel {
        return Gson().fromJson(value, GeocodeUiModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: GeocodeUiModel) {
        bundle.putParcelable(key, value)
    }
}