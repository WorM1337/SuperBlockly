package com.unewexp.superblockly.enums

enum class ExtendConnectionViewType {
    SIDE, // Для INPUT - Увеличивает height блока так, чтобы он был высотой как приклеиваемый, но если приклеиваемый меньше, то ничего не меняется.
    // Формула увеличения - max(height_connect - height_block, 0.0)
    INNER, // Для INPUT - Увеличивает все размеры блока (width и height) на размеры приклеиваемого блока
    INNER_BOTTOM, // Для STRING_BOTTOM_INNER - он должен задавать родителю только высоту приклеиваемого блока
    NONE // Для OUTPUT, STRING_TOP - должны иметь какой-то тип, но смотреть будем только на input-коннекторы. STRING_BOTTOM_OUTER -
    // ничего не должен делать
}