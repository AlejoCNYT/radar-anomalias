async function loadAlerts() {
    const res = await fetch("http://localhost:8082/alerts");
    const data = await res.json();

    document.getElementById("alerts").innerHTML =
        data.length === 0 ? "No hay alertas" :
        data.map(a => `<p><b>${a.service}</b>: ${a.value} (detector: ${a.detector})</p>`).join("");
}

setInterval(loadAlerts, 5000);
