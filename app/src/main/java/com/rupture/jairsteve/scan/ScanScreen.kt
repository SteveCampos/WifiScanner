package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult
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

//Stateful Version
@Composable
fun ScanScreen(scanViewModel: ScanViewModel = hiltViewModel()) {
    val state = scanViewModel.scanState.collectAsState().value
    ScanScreen(
        state = state
    )
}

@Composable
fun ScanScreen(state: ScanState) {
    when (state) {
        is ScanState.SuccessScan -> SuccessScanScreen(state)
        is ScanState.SecurityExceptionOnScan -> FailedScanScreen(state)
        is ScanState.PerformingScan -> LoadingScanScreen()
        is ScanState.StartScanFailed -> StartScanFailedScreen(state)
    }
}

@Composable
fun StartScanFailedScreen(state: ScanState.StartScanFailed) {
    ErrorWidget(tryAgain = { state.tryAgain() })
}

@Composable
fun LoadingScanScreen() {
    LoadingWidget()
}

@Composable
fun SuccessScanScreen(scanState: ScanState.SuccessScan) {
    LazyColumn {
        items(scanState.items) { item ->
            ScanItem(item)
        }
    }
}

@Composable
fun ScanItem(scanItem: ScanResult) {
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
                text = scanItem.BSSID,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(R.color.PersonalizadoSteve4)
            )
            Text(
                text = scanItem.capabilities,
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
fun FailedScanScreen(scanState: ScanState.SecurityExceptionOnScan) {
    ErrorWidget(tryAgain = { scanState.tryAgain() })
}
