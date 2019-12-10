package `in`.org.projecteka.jataayu.module

import `in`.org.projecteka.jataayu.BuildConfig
import `in`.org.projecteka.jataayu.network.createNetworkClient
import `in`.org.projecteka.jataayu.provider.remote.ProviderSearchApi
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModelModule = module {
    viewModel {
        ProviderSearchViewModel(
            providerRepository = get()
        )
    }
}

val repositoryModule = module {
    factory { ProviderRepositoryImpl(providerSearchApi = get()) as ProviderRepository }
}

val networkModule = module {
    single { providerSearchApi }
}

private const val BASE_URL = "http://10.0.2.2:8000/"

private val retrofit: Retrofit = createNetworkClient(BASE_URL, BuildConfig.DEBUG)

private val providerSearchApi: ProviderSearchApi = retrofit.create(ProviderSearchApi::class.java)