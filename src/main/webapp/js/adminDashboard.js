document.addEventListener("DOMContentLoaded", () => {
    // 1. Fetch all incidents
    fetch("/A3_4739/admin/incidents", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(incidents => {
            console.log("Incidents:", incidents);
            renderIncidents(incidents);
        })
        .catch(err => {
            console.error("Error fetching incidents:", err);
        });
});

// 2. Render the list of incidents
function renderIncidents(incidents) {
    const container = document.getElementById("incidents-container");
    container.innerHTML = ""; // Clear existing

    incidents.forEach(incident => {
        const incidentDiv = document.createElement("div");

        // Note the use of `incident.incident_id`
        incidentDiv.innerHTML = `
            <p><strong>ID:</strong> ${incident.incident_id}</p>
            <p><strong>Description:</strong> ${incident.description}</p>
            <p><strong>Status:</strong> <span id="status-${incident.incident_id}">${incident.status}</span></p>
            <p><strong>Danger:</strong> <span id="danger-${incident.incident_id}">${incident.danger || "N/A"}</span></p>
        `;

        // Buttons to update status
        const fakeButton = document.createElement("button");
        fakeButton.textContent = "Mark as Fake";
        // Pass the incident.incident_id
        fakeButton.addEventListener("click", () => updateIncident(incident.incident_id, "fake"));

        const runningButton = document.createElement("button");
        runningButton.textContent = "Mark as Running";
        runningButton.addEventListener("click", () => updateIncident(incident.incident_id, "running"));

        const finishedButton = document.createElement("button");
        finishedButton.textContent = "Mark as Finished";
        finishedButton.addEventListener("click", () => updateIncident(incident.incident_id, "finished"));

        // Danger input
        const dangerLabel = document.createElement("label");
        dangerLabel.textContent = "Danger: ";
        const dangerInput = document.createElement("input");
        dangerInput.type = "text";
        dangerInput.placeholder = "low, medium, high...";

        const dangerBtn = document.createElement("button");
        dangerBtn.textContent = "Update Danger";
        // Pass the incident.incident_id
        dangerBtn.addEventListener("click", () => updateIncidentDanger(incident.incident_id, dangerInput.value));

        // Append all
        incidentDiv.appendChild(fakeButton);
        incidentDiv.appendChild(runningButton);
        incidentDiv.appendChild(finishedButton);
        incidentDiv.appendChild(document.createElement("br"));
        incidentDiv.appendChild(dangerLabel);
        incidentDiv.appendChild(dangerInput);
        incidentDiv.appendChild(dangerBtn);

        container.appendChild(incidentDiv);
        container.appendChild(document.createElement("hr"));
    });
}

// 3. Update status
function updateIncident(incidentId, newStatus) {
    fetch("/A3_4739/admin/incidents/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            // Must match your servlet's expected JSON key
            incident_id: incidentId,
            status: newStatus
        })
    })
        .then(res => res.json())
        .then(data => {
            console.log("Update response:", data);
            if (data.success) {
                // Update the UI in-place
                document.getElementById(`status-${incidentId}`).textContent = newStatus;
            } else {
                alert("Failed to update incident status: " + data.message);
            }
        })
        .catch(err => {
            console.error("Error updating incident:", err);
        });
}

// 4. Update danger
function updateIncidentDanger(incidentId, dangerValue) {
    fetch("/A3_4739/admin/incidents/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            incident_id: incidentId,
            danger: dangerValue
        })
    })
        .then(res => res.json())
        .then(data => {
            console.log("Update response:", data);
            if (data.success) {
                document.getElementById(`danger-${incidentId}`).textContent = dangerValue;
            } else {
                alert("Failed to update danger level: " + data.message);
            }
        })
        .catch(err => {
            console.error("Error updating incident:", err);
        });
}

document.addEventListener('DOMContentLoaded', () => {
    const adminAddIncidentForm = document.getElementById('adminAddIncidentForm');
    if (!adminAddIncidentForm) return; // If form not found, do nothing

    adminAddIncidentForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Gather form data
        const formData = new FormData(adminAddIncidentForm);
        const incidentData = {};

        formData.forEach((value, key) => {
            // Trim all incoming values
            const trimmedVal = value.trim();
            incidentData[key] = trimmedVal;
        });

        // If you have numeric fields, parse them:
        if (incidentData.lat) {
            incidentData.lat = parseFloat(incidentData.lat);
        }
        if (incidentData.lon) {
            incidentData.lon = parseFloat(incidentData.lon);
        }
        if (incidentData.vehicles) {
            incidentData.vehicles = parseInt(incidentData.vehicles, 10);
        }
        if (incidentData.firemen) {
            incidentData.firemen = parseInt(incidentData.firemen, 10);
        }

        try {
            // POST to your servlet endpoint
            const response = await fetch("/A3_4739/admin/incidents/new", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(incidentData)
            });

            const result = await response.json();

            // Check success
            if (response.ok && result.success) {
                document.getElementById('adminAddIncidentResult').textContent =
                    "Το συμβάν καταχωρήθηκε επιτυχώς!";

                // (Optional) Re-fetch incidents if you have a function for that:
                // fetchIncidents();
                window.location.reload();
            } else {
                // Show error returned by server
                document.getElementById('adminAddIncidentResult').textContent =
                    "Σφάλμα: " + (result.message || "Αποτυχία καταχώρησης συμβάντος.");
            }
        } catch (err) {
            console.error("Error adding incident:", err);
            document.getElementById('adminAddIncidentResult').textContent =
                "Σφάλμα σύνδεσης ή σφάλμα διακομιστή.";
        }
    });
});


document.addEventListener('DOMContentLoaded', function () {
    const createVolunteerPositionForm = document.getElementById('createVolunteerPositionForm');

    if (createVolunteerPositionForm) {
        createVolunteerPositionForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            // Collect form data
            const formData = new FormData(createVolunteerPositionForm);
            const data = {};
            formData.forEach((value, key) => {
                data[key] = value;
            });

            try {
                // Display loading message
                document.getElementById('createVolunteerPositionResult').textContent =
                    'Δημιουργία θέσης...';

                // Send POST request to the server
                const response = await fetch('/A3_4739/admin/volunteerPositions/new', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                });

                const result = await response.json();

                if (response.ok && result.success) {
                    // Success message
                    document.getElementById('createVolunteerPositionResult').textContent =
                        'Θέση εθελοντών δημιουργήθηκε επιτυχώς!';

                    // Clear form fields
                    createVolunteerPositionForm.reset();

                    // Optionally fetch updated volunteer positions
                    // Uncomment the following if you have a fetchVolunteerPositions function
                    // fetchVolunteerPositions();
                } else {
                    // Error message from the server
                    document.getElementById('createVolunteerPositionResult').textContent =
                        'Σφάλμα: ' + (result.message || 'Αποτυχία δημιουργίας θέσης.');
                }
            } catch (error) {
                // Connection or server error
                console.error('Error:', error);
                document.getElementById('createVolunteerPositionResult').textContent =
                    'Σφάλμα διακομιστή. Παρακαλώ προσπαθήστε ξανά.';
            }
        });
    }
});

