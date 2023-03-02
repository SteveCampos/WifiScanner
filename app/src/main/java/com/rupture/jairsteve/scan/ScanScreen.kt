package com.rupture.jairsteve.scan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onScanItemClicked: (MyScanResult) -> Unit,
    scanViewModel: ScanViewModel = hiltViewModel(),
    savedNetworksViewModel: SavedNetworksViewModel = hiltViewModel()
) {
    val scanState = scanViewModel.scanState.collectAsState(ScanState.PerformingScan()).value
    val savedNetworksState = savedNetworksViewModel.savedNetworksState.collectAsState().value
    ScanScreen(
        scanState = scanState,
        savedNetworksState = savedNetworksState,
        onScanItemClicked = onScanItemClicked,
        onSavedNetworkItemClicked = onScanItemClicked
    )
}

//Stateless Version
@Composable
fun ScanScreen(
    scanState: ScanState<MyScanResult>,
    savedNetworksState: SavedNetworksState,
    onScanItemClicked: (MyScanResult) -> Unit,
    onSavedNetworkItemClicked: (MyScanResult) -> Unit
) {
    Scaffold() {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                ScannedNetworks(scanState, onScanItemClicked)
            }
            item {
                SavedNetworks(savedNetworksState, onSavedNetworkItemClicked)
            }
        }
    }
}

@Composable
fun SavedNetworks(
    savedNetworksState: SavedNetworksState,
    onSavedNetworkItemClicked: (MyScanResult) -> Unit
) {
    when (savedNetworksState) {
        is SavedNetworksState.Loading -> SavedNetworksLoading()
        is SavedNetworksState.Error -> SavedNetworksError(savedNetworksState)
        is SavedNetworksState.Success -> SavedNetworksSuccess(
            savedNetworksState,
            onSavedNetworkItemClicked
        )
    }
}

@Composable
fun SavedNetworksSuccess(
    savedNetworksState: SavedNetworksState.Success,
    onSavedNetworkItemClicked: (MyScanResult) -> Unit
) {
    CardSavedItems(items = savedNetworksState.items, onScanItemClicked = onSavedNetworkItemClicked)
}

@Composable
fun SavedNetworksError(savedNetworksState: SavedNetworksState.Error) {
    ErrorWidget(message = "Error al obt4ener las redes escaneadas guardadas en el dispositivo",
        tryAgain = { savedNetworksState.tryAgain() })
}

@Composable
fun SavedNetworksLoading() {
    LoadingWidget()
}

@Composable
fun ScannedNetworks(scanState: ScanState<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit) {
    when (scanState) {
        is ScanState.SuccessScan -> SuccessScanScreen(scanState, onScanItemClicked)
        is ScanState.SecurityExceptionOnScan -> SecurityExceptionScreen(scanState)
        is ScanState.PerformingScan -> LoadingScanScreen()
        is ScanState.StartScanFailed -> StartScanFailedScreen(scanState)
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
    /*Scaffold(backgroundColor = colorResource(id = R.color.BlancoGris), topBar = {
        TopAppBar(backgroundColor = colorResource(id = R.color.BlancoGris), elevation = 0.dp) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_menu_24),
                contentDescription = null,
                modifier = Modifier.padding(start = 32.dp/*, top = 8.dp, bottom = 8.dp, end = 32.dp*/)
            )
        }
    }) {*/
    Column(
        modifier = Modifier
            .padding(16.dp)
        //.padding(it)
    ) {

        Text(
            text = "${scanState.items.size}",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = stringResource(id = R.string.msg_network_founded),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            Text(
                text = "San Miguel",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 16.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_location_on_24),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.padding(vertical = 24.dp)) {

            CardScannedItems(items = scanState.items, onScanItemClicked = onScanItemClicked)
            Spacer(modifier = Modifier.height(24.dp))

        }
    }
    //}
}

@Composable
fun CardSavedItems(items: List<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit) {
    Card(
        backgroundColor = Color.White, shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(bottom = 8.dp)) {

            Text(
                text = stringResource(id = R.string.msg_saved_scanned_networks),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                color = Color(R.color.PersonalizadoSteve4),
                modifier = Modifier.padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp)
            )
            items.map { item ->
                ScanItem(item, modifier = Modifier.clickable { onScanItemClicked(item) })
                Divider(startIndent = 32.dp)
            }
        }
    }
}

@Composable
fun CardScannedItems(items: List<MyScanResult>, onScanItemClicked: (MyScanResult) -> Unit) {
    Card(
        backgroundColor = Color.White, shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(bottom = 8.dp)) {

            Text(
                text = stringResource(id = R.string.msg_scanned),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                color = Color(R.color.PersonalizadoSteve4),
                modifier = Modifier.padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp)
            )
            items.map { item ->
                ScanItem(item, modifier = Modifier.clickable { onScanItemClicked(item) })
                Divider(startIndent = 32.dp)
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
        /*
        Icon(
            painter = painterResource(id = if (scanItem.canCalculateDefaultPassword()) R.ldrawable.ic_done_black_48dp else R.drawable.ic_circle),
            contentDescription = null
        )
        */
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = scanItem.ssid,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )
            /*Text(
                text = scanItem.bssid,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )*/
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
