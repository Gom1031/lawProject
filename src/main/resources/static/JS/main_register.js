document.querySelector('form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const data = {
        username: this.elements.username.value,
        password: this.elements.password.value,
        email: this.elements.email.value,
        phone_number: this.elements.phone_number.value
    };
    const response = await fetch('/api/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    if (response.ok) {
        alert('User registered successfully');
        location.href = '/user/login';
    } else {
        const error = await response.json();
        alert('Error: ' + error.message);
    }
});
