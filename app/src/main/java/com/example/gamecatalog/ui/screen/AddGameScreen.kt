package com.example.gamecatalog.ui.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gamecatalog.ui.GameViewModel

@Composable
fun AddGameScreen(
    onDone: () -> Unit,
    vm: GameViewModel
) {
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("RPG") }
    var nota by remember { mutableStateOf(5) }
    var descricao by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Picker usando OpenDocument (permite URI persistente)
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            // Garantir permissão persistente da imagem
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}

            imageUri = uri.toString()
        }
    }

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Gênero (RPG/FPS/ESPORTES)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nota.toString(),
                onValueChange = { text ->
                    text.toIntOrNull()?.let { n -> nota = n.coerceIn(0, 10) }
                },
                label = { Text("Nota (0-10)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Capa do jogo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = {
                        picker.launch(arrayOf("image/*"))
                    }
                ) {
                    Text(if (imageUri == null) "Escolher imagem" else "Trocar imagem")
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        vm.addGame(
                            nome.trim(),
                            genero.trim().uppercase(),
                            nota,
                            descricao.trim(),
                            imageUri
                        )
                        onDone()
                    },
                    enabled = nome.isNotBlank()
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
