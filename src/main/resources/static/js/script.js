// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', (event) => {
    // Select the form element
    const signupForm = document.getElementById('signupForm');

    // Add an event listener for the form submission
    signupForm.addEventListener('submit', function (e) {
        // Prevent the default form submission
        e.preventDefault();

        // Get input values
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const username = document.getElementById('username').value;
        const email = document.getElementById('userEmail').value;
        const password = document.getElementById('userPassword').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        // Get error message elements
        const usernameError = document.getElementById('usernameError');
        const firstNameError = document.getElementById('firstNameError');
        const lastNameError = document.getElementById('lastNameError');
        const emailError = document.getElementById('emailError');
        const passwordError = document.getElementById('passwordError');
        const confirmPasswordError = document.getElementById('confirmPasswordError');

        // Reset previous error messages
        usernameError.textContent = '';
        firstNameError.textContent = '';
        lastNameError.textContent = '';
        emailError.textContent = '';
        passwordError.textContent = '';
        confirmPasswordError.textContent = '';

        let isValid = true;

        // Validation logic
        if (username.trim() === '') {
            fullNameError.textContent = 'Username is required.';
            isValid = false;
        }
        if (firstName.trim() === '') {
            firstNameError.textContent = 'First name is required.';
            isValid = false;
        }
        if (lastName.trim() === '') {
            lastNameError.textContent = 'Last name is required.';
            isValid = false;
        }
        if (email.trim() === '') {
            emailError.textContent = 'Email address is required.';
            isValid = false;
        } else if (!/^\S+@\S+\.\S+$/.test(email)) {
            emailError.textContent = 'Please enter a valid email format.';
            isValid = false;
        }

        if (password.length < 8) {
            passwordError.textContent = 'Password must be at least 8 characters.';
            isValid = false;
        }

        if (password !== confirmPassword) {
            confirmPasswordError.textContent = 'Passwords do not match.';
            isValid = false;
        }

        const data = {
            firstName,
            lastName,
            username,
            email,
            password
        }
        // If all validations pass, you would typically send data to a server
        if (isValid) {
            if (password == confirmPassword) {
                const jsonData = JSON.stringify(data);
                fetch('/portal/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    redirect: 'manual',
                    body: jsonData
                })
                    .then(response => {
                        console.log('Status Code: ' + response.status);
                    })
                alert('Form submitted successfully!');
            } else {
                alert('Password entered was incorrect. Please try again.');
            }
        }
    });
});