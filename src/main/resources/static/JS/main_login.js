document.querySelector('form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const data = {
        username: this.elements.username.value,
        password: this.elements.password.value
    };
    const response = await fetch('/api/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    if (response.ok) {
        alert('User logged in successfully');
        location.href = '/consultboard';
    } else {
        const errorObj = await response.json();  // Convert response to JSON
        alert('Error: ' + errorObj.error);  // Access the error message
    }            
});