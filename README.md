# Komak Dast - Android

# Deprecated

Migrated to [Kotlin](https://github.com/lrhm/komak-dast-ktln)

Persian Sign Language Learning Assistant Native Android Application for children with the use of visual learning techniques by providing videos and images.

## TODO
* Refactor, Refactor and Refactor whole lot more. convert to Kotlin
* use static file server to reduce the app size


## Adding Packages
Adding packages are easy, there are three locations you need to know.
* `res/raw`
* `assets/Packages`

### `res/raw`
In this folder you need to define the package, look at the `local.json` file and add another package accordingly, you need to add a picture for each package too, the format is `package_$id_front.png`

### `assets/Packages`
In this folder, the package exists, each package must have a `levels.json` file that defines the levels, and subfolder containing images and videos


## License
### GNU
