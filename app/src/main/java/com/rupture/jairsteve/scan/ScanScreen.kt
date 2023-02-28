package com.rupture.jairsteve.scan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
fun ScanScreen(
    onScanItemClicked: (MyScanResult) -> Unit, scanViewModel: ScanViewModel = hiltViewModel()
) {
    val state = scanViewModel.scanState.collectAsState(ScanState.PerformingScan()).value
    ScanScreen(
        state = state, onScanItemClicked
    )
}

//Stateless Version
@Composable
fun ScanScreen(
    state: ScanState<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit
) {
    when (state) {
        is ScanState.SuccessScan -> SuccessScanScreen(state, onScanItemClicked)
        is ScanState.SecurityExceptionOnScan -> SecurityExceptionScreen(state)
        is ScanState.PerformingScan -> LoadingScanScreen()
        is ScanState.StartScanFailed -> StartScanFailedScreen(state)
    }
}

@Composable
fun StartScanFailedScreen(state: ScanState.StartScanFailed<MyScanResult>) {
    ErrorWidget(message = "Falló el inicio del escaneo. Esto puede deberse a que la ubicación no está habilitada, r2, r3, etc.",
        tryAgain = { state.tryAgain() })
}

@Composable
fun LoadingScanScreen() {
    LoadingWidget()
}

@Composable
fun SuccessScanScreen(
    scanState: ScanState.SuccessScan<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit
) {

    Scaffold(backgroundColor = colorResource(id = R.color.BlancoGris)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(it)
        ) {
            Text(text = "${scanState.items.size}", style = MaterialTheme.typography.h1)
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = stringResource(id = R.string.msg_network_founded),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row() {
                Text(text = "San Miguel", style = MaterialTheme.typography.body1)
                Icon(
                    painter = painterResource(id = R.drawable.ic_signal),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            CardScannedItems(items = scanState.items, onScanItemClicked = onScanItemClicked)
        }
    }
}

@Composable
fun CardScannedItems(items: List<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit) {
    Card(
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        LazyColumn(Modifier.padding(bottom = 8.dp)) {
            item {
                Text(
                    text = stringResource(id = R.string.msg_scanned),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(R.color.PersonalizadoSteve4),
                    modifier = Modifier.padding(start = 32.dp, top = 16.dp, bottom = 16.dp)
                )
            }
            items(items) { item ->
                ScanItem(item, modifier = Modifier.clickable { onScanItemClicked(item) })
            }
        }
    }
}

@Composable
fun ScanItem(scanItem: MyScanResult, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*Icon(
            painter = painterResource(id = if (scanItem.canCalculateDefaultPassword()) R.drawable.ic_done_black_48dp else R.drawable.ic_circle),
            contentDescription = null
        )*/
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = scanItem.ssid,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )
            Text(
                text = scanItem.bssid,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )
            Text(
                text = scanItem.vendorName ?: stringResource(id = R.string.msg_vendor_unknown),
                style = MaterialTheme.typography.body1,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color(R.color.PersonalizadoSteve4)
            )
            Text(
                text = scanItem.getDefaultPassword().orEmpty(),
                style = MaterialTheme.typography.body1,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color(R.color.PersonalizadoSteve4)
            )
        }
        Column() {
            Icon(
                painter = painterResource(id = R.drawable.ic_signal_cellular_3_bar_black_36dp),
                modifier = Modifier.size(24.dp),
                contentDescription = null
            )
            Text(text = "Bueno", style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
fun SecurityExceptionScreen(scanState: ScanState.SecurityExceptionOnScan<MyScanResult>) {
    ErrorWidget(message = "SecurityException (Fallo al escanear las redes disponibles). Esto puede deberse a que no se dieron permisos de Ubicación, Cambia estado de Wifi, etc",
        tryAgain = { scanState.tryAgain() })
}
