@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.gamecatalog.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gamecatalog.ui.GameViewModel

@Composable
fun DetailScreen(onBack: () -> Unit, vm: GameViewModel, id: String) {
    val state by vm.state.collectAsState()
    val game = remember(state.games, id) { state.games.find { it.id == id } }

    var showObservationField by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game?.nome ?: "Detalhes do Jogo") },
                actions = {
                    OutlinedButton(
                        onClick = onBack,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (game == null) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            var observationText by remember(game.id) {
                mutableStateOf(game.observacao ?: "")
            }

            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (game.imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(game.imageUri),
                        contentDescription = "Capa de ${game.nome}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    "Gênero: ${game.genero} • Nota: ${game.nota}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider()
                Text("Descrição", style = MaterialTheme.typography.titleSmall)
                Text(
                    if (game.descricao.isNotBlank()) game.descricao else "Sem descrição.",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (!game.observacao.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Observação", style = MaterialTheme.typography.titleSmall)
                    Text(game.observacao!!, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { showObservationField = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (!game.observacao.isNullOrBlank())
                            "Editar observação"
                        else
                            "Adicionar observação"
                    )
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir jogo")
                }

                if (showObservationField) {
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = observationText,
                        onValueChange = { observationText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Escreva sua observação") },
                        minLines = 3
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showObservationField = false }) {
                            Text("Cancelar")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                vm.updateObservation(game.id, observationText)
                                showObservationField = false
                            }
                        ) {
                            Text("Salvar")
                        }
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Remover jogo") },
                    text = { Text("Tem certeza que deseja excluir \"${game.nome}\" do catálogo?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                vm.deleteGame(game.id) {
                                    onBack()
                                }
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Sim, excluir", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
