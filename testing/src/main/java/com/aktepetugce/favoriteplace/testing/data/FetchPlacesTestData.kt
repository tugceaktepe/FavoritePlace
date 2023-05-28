package com.aktepetugce.favoriteplace.testing.data

import com.aktepetugce.favoriteplace.data.model.Place

val places: List<Place> = listOf(
    Place(
        id = "12345",
        name = "Warner Bros. : Harry Potter Studio",
        description = "Making of Harry Potter",
        feeling = 5,
        imageUrl = "https://s32508.pcdn.co/wp-content/uploads/2019/06/Hogwarts-Castle-Model.jpg",
        latitude = "51.690223125643016",
        longitude = "-0.418723663967239"
    ),
    Place(
        id = "12345",
        name = "Tower Bridge",
        description = "London Tower Bridge",
        feeling = 5,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb" +
            "/6/63/Tower_Bridge_from_Shad_Thames.jpg/600px-Tower_Bridge_from_Shad_Thames.jpg",
        latitude = "51.50559661081221",
        longitude = "-0.0753994179469388"

    ),
    Place(
        id = "12345",
        name = "Kensington Gardens",
        description = "Once the private gardens of Kensington Palace",
        feeling = 5,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/" +
            "thumb/4/47/Kensington_Palace_from_across_Long_" +
            "Water.JPG/1280px-Kensington_Palace_from_across_Long_Water.JPG",
        latitude = "-0.1793342805287279",
        longitude = "51.50726143894236"
    )
)
