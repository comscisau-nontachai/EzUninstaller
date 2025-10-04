package nontachai.becomedev.uninstaller.di
import nontachai.becomedev.uninstaller.presentation.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //single { UserRepository() }


    viewModel { HomeViewModel(androidContext()) }
}