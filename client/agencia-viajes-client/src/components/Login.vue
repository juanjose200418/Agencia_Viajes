<template>
    <v-sheet class="mx-auto" width="400">
      <v-form @submit.prevent="submitForm" fast-fail>
  
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
  
        <v-btn class="mt-2" type="submit" :disabled="passwordMismatch || !formIsValid" block>
          Iniciar Sesión
        </v-btn>
  
        <v-alert
          v-if="errorMessage"
          type="error"
          class="mt-3"
          prominent
        >
          {{ errorMessage }}
        </v-alert>
  
        <v-alert
          v-if="successMessage"
          type="success"
          class="mt-3"
          prominent
        >
          {{ successMessage }}
        </v-alert>
      </v-form>
    </v-sheet>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { User } from '../models/User';
  import UserService from '../services/UserService';
  
  const user = ref<User>(new User());
  const confirmPassword = ref('');
  const errorMessage = ref<string | null>(null);
  const successMessage = ref<string | null>(null);
  
  // Reglas de validación
  const emailRules = [
    (v: string) => !!v || 'Correo electrónico es obligatorio',
    (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido'
  ];
  
  const passwordRules = [
    (v: string) => !!v || 'Contraseña es obligatoria',
    (v: string) => (v && v.length >= 6) || 'La contraseña debe tener al menos 6 caracteres'
  ];
  
  
  const passwordMismatch = computed(() => user.value.pass !== confirmPassword.value);
  const formIsValid = computed(() => {
    return user.value.firstname && user.value.lastname && 
           user.value.email && user.value.pass && 
           !passwordMismatch.value;
  });
  
  const submitForm = async () => {
    if (!passwordMismatch.value) {
      try {
        const response = await UserService.create(user.value);
        successMessage.value = 'Usuario con éxito';
        errorMessage.value = null;
      } catch (error : unknown) {
        errorMessage.value = (error as any).message || 'Error al registrar usuario';
        successMessage.value = null;
      }
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
  
  input,
  select {
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
  