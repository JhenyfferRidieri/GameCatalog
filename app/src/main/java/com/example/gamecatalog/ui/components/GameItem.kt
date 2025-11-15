package com.example.gamecatalog.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gamecatalog.data.model.GameDTO

@Composable
fun GameItem(game: GameDTO, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(game.id) }
    ) {
        Row(Modifier.padding(12.dp)) {
            if (game.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(game.imageUri),
                    contentDescription = "Capa de ${game.nome}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .heightIn(min = 180.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column(Modifier.weight(1f)) {
                Text(game.nome, style = MaterialTheme.typography.titleMedium)
                Text("Gênero: ${game.genero} • Nota: ${game.nota}")
            }
        }
    }
}
