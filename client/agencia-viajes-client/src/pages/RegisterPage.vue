
<template>
  <div class="register-container">
    <h1>User Registration</h1>

    <form @submit.prevent="registerUser">
     
      <div>
        <label for="firstname">First Name:</label>
        <input v-model="user.firstname" id="firstname" type="text" required />
      </div>

   
      <div>
        <label for="lastname">Last Name:</label>
        <input v-model="user.lastname" id="lastname" type="text" required />
      </div>

     
      <div>
        <label for="email">Email:</label>
        <input v-model="user.email" id="email" type="email" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input v-model="user.pass" id="password" type="password" required />
      </div>

      <div>
        <label for="confirmPassword">Confirm Password:</label>
        <input v-model="confirmPassword" id="confirmPassword" type="password" required />
      </div>

      <div>
        <label for="phone">Phone:</label>
        <input v-model="user.phone" id="phone" type="tel" />
      </div>

      <div>
        <label for="address">Address:</label>
        <input v-model="user.address" id="address" type="text" />
      </div>

      <div>
        <label for="role">Role:</label>
        <select v-model="user.role" id="role" required>
          <option value="ADMIN">Admin</option>
          <option value="USER">User</option>
          <option value="SUPER_ADMIN">Super Admin</option>
        </select>
      </div>

      <button type="submit">Register</button>
    </form>

    <p v-if="message" :class="{ success: isSuccess, error: !isSuccess }">{{ message }}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios';  // Para hacer las peticiones HTTP
import { Role } from '../models/Role'; // Asegúrate de importar tu enumeración Role

/*export default defineComponent({
  data() {
    return {
      user: {
        firstname: 'juanjo',
        lastname: 'jhbfvhdsfv',
        email: 'cghrfvdfgbd@gmail.com',
        pass: '1234',
        phone: '693045683',
        address: 'Calle remedios, el puntal murcia',
        role: "USER" as Role 
      },
      confirmPassword: '',
      message: '',   // Mensaje de éxito o error
      isSuccess: false  // Para mostrar si el registro fue exitoso o no
    };
  },
  methods: {
    // Función para manejar el registro del usuario
    async registerUser() {
      // Verificar que las contraseñas coincidan
      if (this.user.pass !== this.confirmPassword) {
        this.message = 'Passwords do not match';
        this.isSuccess = false;
        return;
      }

      try {
        // Enviar los datos al backend
        const response = await axios.post('http://localhost:8080/auth/register', this.user);

        // Mostrar mensaje de éxito
        this.message = 'User registered successfully!';
        this.isSuccess = true;

        // Limpiar los campos del formulario
        this.user
        this.confirmPassword = '';
      } catch (error) {
        // Manejar errores del backend
        if (axios.isAxiosError(error) && error.response?.data?.message) {
          this.message = error.response.data.message;
        } else {
          this.message = 'An error occurred during registration.';
        }
        this.isSuccess = false;
      }
    }
  }
});*/
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
  color: black; /* Color de texto en negro */
}

label {
  display: block;
  margin-bottom: 8px;
  color: black; /* Letras en negro */
}

input, select {
  width: 100%;
  padding: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  color: black; /* Letras en negro */
}

button {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}

p, h1 {
  color: black; /* Letras en negro */
}

.success {
  color: green;
}

.error {
  color: red;
}
</style>
