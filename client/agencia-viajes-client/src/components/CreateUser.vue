<template>
    <v-sheet class="mx-auto" width="400">
      <v-form @submit.prevent="submitForm" fast-fail>
        <v-text-field
          v-model="user.firstname"
          :rules="nameRules"
          label="Nombre"
          placeholder="Introduce el nombre"
        ></v-text-field>
  
        <v-text-field
          v-model="user.lastname"
          :rules="nameRules"
          label="Apellido"
          placeholder="Introduce el apellido"
        ></v-text-field>
  
        <v-text-field
          v-model="user.email"
          :rules="emailRules"
          label="Correo Electrónico"
          placeholder="Introduce el correo"
          type="email"
          required
        ></v-text-field>
  
        <v-text-field
          v-model="user.pass"
          :rules="passwordRules"
          label="Contraseña"
          placeholder="Introduce la contraseña"
          type="password"
          required
        ></v-text-field>
  
        <v-text-field
          v-model="confirmPassword"
          :rules="[confirmPasswordRule]"
          label="Confirme Contraseña"
          placeholder="Introduce de nuevo la contraseña"
          type="password"
          required
        ></v-text-field>
  
        <v-text-field
          v-model="user.phone"
          label="Teléfono"
          placeholder="Introduce el teléfono"
        ></v-text-field>
  
        <v-text-field
          v-model="user.address"
          label="Dirección"
          placeholder="Introduce la dirección"
        ></v-text-field>
  
        <v-btn class="mt-2" type="submit" :disabled="passwordMismatch" block>
          Registrar
        </v-btn>
  
        <v-alert
          v-if="passwordMismatch"
          type="error"
          class="mt-3"
          prominent
          border="right"
        >
          Las contraseñas no coinciden
        </v-alert>
  
        <v-alert
          v-if="errorMessage"
          type="error"
          class="mt-3"
          prominent
          border="right"
        >
          {{ errorMessage }}
        </v-alert>
  
        <v-alert
          v-if="successMessage"
          type="success"
          class="mt-3"
          prominent
          border="right"
        >
          {{ successMessage }}
        </v-alert>
      </v-form>
    </v-sheet>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { User } from '../models/User';
  import UserService from '../services/UserService'; // Correcta importación del servicio
  
  // Definición de variables reactivas
  const user = ref<User>(new User());
  const confirmPassword = ref('');
  const errorMessage = ref<string | null>(null); // Mensaje de error
  const successMessage = ref<string | null>(null); // Mensaje de éxito
  
  // Computed para validar las contraseñas
  const passwordMismatch = computed(() => user.value.pass !== confirmPassword.value);
  
  // Función para manejar el envío del formulario
  const submitForm = async () => {
    if (!passwordMismatch.value) {
      try {
        // Llama al servicio para crear el usuario
        const response = await UserService.create(user.value);
        successMessage.value = 'Usuario creado con éxito';
        errorMessage.value = null; // Limpia el mensaje de error si el registro es exitoso
        console.log('Usuario creado con éxito:', response);
      } catch (error) {
        // Manejo de errores
        errorMessage.value = `Error al registrar usuario: ${error.message}`;
        successMessage.value = null; // Limpia el mensaje de éxito si ocurre un error
      }
    } else {
      console.log('Las contraseñas no coinciden');
    }
  };
  </script>
  
  <style scoped>
  .user-form {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    background-color: #000;
    border-radius: 10px;
    color: white;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  input, select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  
  button:hover:not(:disabled) {
    background-color: #45a049;
  }
  
  .error-text {
    color: red;
    font-size: 0.9em;
  }
  </style>
  