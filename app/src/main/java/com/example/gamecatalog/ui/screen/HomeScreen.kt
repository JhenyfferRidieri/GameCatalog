package com.example.gamecatalog.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamecatalog.ui.GameViewModel
import com.example.gamecatalog.ui.components.GameRowCard

@Composable
fun HomeScreen(vm: GameViewModel, onAdd: () -> Unit, onOpen: (String) -> Unit) {
    val state by vm.state.collectAsState()

    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = onAdd) { Text("+") } }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Text(
                "Catálogo de Jogos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            if (state.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.games, key = { it.id }) { game ->
                    GameRowCard(game = game, onClick = onOpen) // ou GameItem(...)
                }
            }
        }
    }
}
