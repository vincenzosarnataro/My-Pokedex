<h1 align="center">My Pokedex</h1>

<p align="center">  
My Pokedex is a demo app based on Clean Architecture (Repository pattern) and MVI pattern with Uniflow
</p>
</br>

## Open-source libraries

1. [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous. 
2. [Uniflow](https://github.com/uniflow-kt/uniflow-kt) Simple Unidirectional Data Flow.
3. [Koin](https://insert-koin.io/) for DI.
4. [Moshi](https://github.com/square/moshi/) JSON library.
5. [Retrofit2 & OkHttp3](https://github.com/square/retrofit) for the REST APIs and paging network data.
6. [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette) for loading images.
7. [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for display pages of data from a larger dataset from local storage or over network.
8. [Room](https://developer.android.com/jetpack/androidx/releases/room) persistence library
9. [Why-Not-Image-Carousel](https://github.com/ImaginativeShohag/Why-Not-Image-Carousel) for slidershow images

## Nice features

- [x] Offile
- [x] Unite test
- [x] Android 12 splash screen support
- [x] New Android Jetpack Paging

## Architecture
There are 3 layer in this app. App module import all layer just for resolve the DI in Application Class
| Presentation Layer      | Domain Layer          | Data Layer                         |
| ----------------------- | --------------------- | ---------------------------------- |
| ui/view                 | entity                | data source, dto                   |
| viewmodel               | usecase               | repository implementation          |
| ui model                | repository interface  | library config(retrofit/room)      |
|                         |                       |                                    |

![clean](https://user-images.githubusercontent.com/39265806/139958824-6ca7113d-4029-4d1f-9833-2525ba66d6b9.jpg)

## App Level Example

| Presentation Layer                                     | Something in Between             | Domain Layer                        | Data Layer                         | Outer data layer                   |
| ------------------------------------------------------ | -------------------------------- | ----------------------------------- | ---------------------------------- | ---------------------------------- |
| PokemonDetailActivity & PokemonDetailViewModel         | <- GetPokemonDetailUseCase ->    | <- PokemonRepository (interface) -> | <- PokemonRepositoryImplementation | RemoteDataSource & CacheDataSource |
                                                                                                                                                                       
## Design

- Design is ispired from [Pokedex App design](https://dribbble.com/shots/6540871-Pokedex-App)
