package ru.internetcloud.strava.presentation.navigation

sealed class Screen(
    val route: String
) {
    object Home : Screen(ROUTE_HOME)
    object You : Screen(ROUTE_YOU)
    object TrainingList : Screen(ROUTE_TRAINING_LIST)
    object TrainingDetail : Screen(ROUTE_TRAINING_DETAIL) {
        private const val ROUTE_FOR_ARGS = "training_detail"
        fun getRouteWithArg(id: Long): String {
            return "$ROUTE_FOR_ARGS/$id"
        }
    }
    object TrainingDetailEdit : Screen(ROUTE_TRAINING_DETAIL_EDIT) {
        private const val ROUTE_FOR_ARGS = "training_detail_edit"
        fun getRouteWithArg(id: Long): String {
            return "$ROUTE_FOR_ARGS/$id"
        }
    }

    object TrainingDetailAdd : Screen(ROUTE_TRAINING_DETAIL_ADD)

    object Web : Screen(ROUTE_WEB) {
        private const val ROUTE_FOR_ARGS = "web"
        fun getRouteWithArg(link: String): String {
            return "$ROUTE_FOR_ARGS/$link"
        }
    }

    object WebDeepLink : Screen(ROUTE_WEBDEEPLINK)

    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_WEB = "web/{stravaLink}"
        const val ROUTE_WEBDEEPLINK = "webdeeplink"
        const val ROUTE_YOU = "you"

        const val ROUTE_TRAINING_LIST = "training_list"
        const val ROUTE_TRAINING_DETAIL = "training_detail/{id}"
        const val ROUTE_TRAINING_DETAIL_EDIT = "training_detail_edit/{id}"
        const val ROUTE_TRAINING_DETAIL_ADD = "training_detail_add"

        const val KEY_ID = "id"
        const val KEY_LINK = "stravaLink"
    }
}
