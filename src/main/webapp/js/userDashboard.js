document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch("/A3_4739/getUserDetails");
        const data = await response.json();

        if (data.success) {
            const fields = [
                "username", "email", "firstname", "lastname", "birthdate", "gender",
                "country", "address", "municipality", "prefecture", "job",
                "telephone", "afm", "latitude", "longitude"
            ];

            fields.forEach(field => {
                document.getElementById(`${field}Display`).innerText = data[field] || "";
                if (document.getElementById(field)) {
                    document.getElementById(field).value = data[field] || "";
                }
            });
        } else {
            console.error("Failed to fetch user details:", data.message);
        }
    } catch (error) {
        console.error("Error fetching user details:", error);
    }
});