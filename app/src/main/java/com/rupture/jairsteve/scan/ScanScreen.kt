package com.rupture.jairsteve.scan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rupture.jairsteve.designsystem.component.ErrorWidget
import com.rupture.jairsteve.designsystem.component.LoadingWidget
import com.rupture.jairsteve.rupture.R
import com.rupture.jairsteve.scan.entity.MyScanResult

//Stateful Version
@Composable
fun ScanScreen(scanViewModel: ScanViewModel = hiltViewModel()) {
    val state =
        scanViewModel.scanState.collectAsState(ScanState.PerformingScan()).value
    ScanScreen(
        state = state
    )
}

//Stateless Version
@Composable
fun ScanScreen(state: ScanState<MyScanResult>) {
    when (state) {
        is ScanState.SuccessScan -> SuccessScanScreen(state)
        is ScanState.SecurityExceptionOnScan -> SecurityExceptionScreen(state)
        is ScanState.PerformingScan -> LoadingScanScreen()
        is ScanState.StartScanFailed -> StartScanFailedScreen(state)
    }
}

@Composable
fun StartScanFailedScreen(state: ScanState.StartScanFailed<MyScanResult>) {
    ErrorWidget(
        message = "Falló el inicio del escaneo. Esto puede deberse a que la ubicación no está habilitada, r2, r3, etc.",
        tryAgain = { state.tryAgain() }
    )
}

@Composable
fun LoadingScanScreen() {
    LoadingWidget()
}

@Composable
fun SuccessScanScreen(scanState: ScanState.SuccessScan<MyScanResult>) {

    LazyColumn {
        items(scanState.items) { item ->
            ScanItem(item)
        }
    }
}

@Composable
fun ScanItem(scanItem: MyScanResult) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_done_black_48dp),
            contentDescription = null
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = scanItem.bssid,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )
            Text(
                text = scanItem.vendor.orEmpty(),
                style = MaterialTheme.typography.body1,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color(R.color.PersonalizadoSteve4)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_signal_cellular_3_bar_black_36dp),
            contentDescription = null
        )
    }
}

@Composable
fun SecurityExceptionScreen(scanState: ScanState.SecurityExceptionOnScan<MyScanResult>) {
    ErrorWidget(
        message = "SecurityException (Fallo al escanear las redes disponibles). Esto puede deberse a que no se dieron permisos de Ubicación, Cambia estado de Wifi, etc",
        tryAgain = { scanState.tryAgain() })
}
