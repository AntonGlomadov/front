package com.haberturm.hitchhikingapp.ui.searchDirection

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.views.LetsGoButton
import com.haberturm.hitchhikingapp.ui.views.SearchField
import com.haberturm.hitchhikingapp.ui.views.SearchRow

const val KEY_SEARCH_DIRECTION_INDEX = "SEARCH_DIRECTION_INDEX"

object SearchDirectionRoute : NavRoute<SearchDirectionViewModel> {
    override val route: String = "search_direction/{$KEY_SEARCH_DIRECTION_INDEX}/"

    /**
     * Returns the route that can be used for navigating to this page.
     */
    fun get(index: Int): String = route.replace("{$KEY_SEARCH_DIRECTION_INDEX}", "$index")

    /*
    not used yet

    fun getIndexFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<Int>(KEY_CONTENT_PAGE_INDEX)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(KEY_CONTENT_PAGE_INDEX) { type = NavType.IntType })
    */
    @Composable
    override fun Content(viewModel: SearchDirectionViewModel) = SearchDirection(viewModel)

    @Composable
    override fun viewModel(): SearchDirectionViewModel = hiltViewModel()

}

@Composable
fun SearchDirection(
    viewModel: SearchDirectionViewModel
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.secondaryVariant)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = "back_button",
                modifier = Modifier.clickable {
                    Log.i("LOG_DEBUG", "ON BACK PRESSED")

                    viewModel.onEvent(SearchDirectionEvent.OnNavigateUpClicked)
                }
            )
            Text(
                text = "Выбор маршрута",
                fontSize = 28.sp,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SearchRow(iconId = R.drawable.ic_baseline_navigation_24)
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                Canvas(Modifier.fillMaxWidth().height(88.dp).width(10.dp).padding(start = 24.dp)) {

                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        pathEffect = pathEffect,
                        strokeWidth = 10f,
                    )
                }
//                Divider(
//                    color = Color.Red,
//                    modifier = Modifier
//                        .height(88.dp)
//                        .width(3.dp),
//                )
                SearchRow(iconId = R.drawable.ic_baseline_flag_24)
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                LetsGoButton()
            }
        }
    }
}
