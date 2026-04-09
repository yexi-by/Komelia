package snd.komelia.db.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import snd.komelia.db.SettingsStateWrapper
import snd.komelia.homefilters.HomeScreenFilter
import snd.komelia.homefilters.HomeScreenFilterRepository
import snd.komelia.homefilters.normalizeForPersistence

class HomeScreenFilterRepositoryWrapper(
    private val wrapper: SettingsStateWrapper<List<HomeScreenFilter>>,
) : HomeScreenFilterRepository {

    override fun getFilters(): Flow<List<HomeScreenFilter>> {
        return wrapper.state
            .map { it.normalizeForPersistence() }
            .onEach { normalized ->
                if (normalized != wrapper.state.value) {
                    wrapper.transform { normalized }
                }
            }
            .distinctUntilChanged()
    }

    override suspend fun putFilters(filters: List<HomeScreenFilter>) {
        wrapper.transform { filters.normalizeForPersistence() }
    }
}
