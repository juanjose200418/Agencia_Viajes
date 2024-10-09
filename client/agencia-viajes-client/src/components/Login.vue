<template>
  <v-sheet class="mx-auto" width="400">
    <v-form @submit.prevent="submitForm" fast-fail>
      <v-text-field
        v-model="credential.email"
        :rules="emailRules"
        label="Correo Electrónico"
        placeholder="Introduce el correo"
        type="email"
        required
      ></v-text-field>

      <v-text-field
        v-model="credential.password"
        :rules="passwordRules"
        label="Contraseña"
        placeholder="Introduce la contraseña"
        type="password"
        required
      ></v-text-field>

      <v-btn class="mt-2" type="submit" :disabled="!formIsValid" block>
        Iniciar Sesión
      </v-btn>

      <v-alert v-if="errorMessage" type="error" class="mt-3" prominent>
        {{ errorMessage }}
      </v-alert>

      <v-alert v-if="successMessage" type="success" class="mt-3" prominent>
        {{ successMessage }}
      </v-alert>
    </v-form>
  </v-sheet>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';  
import { Credential } from '../models/Credential';
import UserService from '../services/UserService';

const router = useRouter();

const credential = ref<Credential>(new Credential('', ''));
const errorMessage = ref<string | null>(null);
const successMessage = ref<string | null>(null);

// Reglas de validación para el formulario
const emailRules = [
  (v: string) => !!v || 'Correo electrónico es obligatorio',
  (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido',
];

const passwordRules = [
  (v: string) => !!v || 'Contraseña es obligatoria',
  (v: string) => (v && v.length >= 6) || 'La contraseña debe tener al menos 6 caracteres',
];

// Computed para validar el formulario
const formIsValid = computed(() => {
  return !!credential.value.email && !!credential.value.password;
});

// Método para enviar el formulario
const submitForm = async () => {
  try {
    // Llamada al servicio de autenticación
    const response = await UserService.login(credential.value);
    const { token, user } = response;

    // Almacenar el token y los datos del usuario en localStorage
    localStorage.setItem('authToken', token);
    localStorage.setItem('user', JSON.stringify(user));

    // Mostrar mensaje de éxito
    successMessage.value = 'Inicio de sesión exitoso!';
    errorMessage.value = null;

    router.push('/');
    
  } catch (error: unknown) {
    // Mostrar error
    errorMessage.value = (error as any).message || 'Error al iniciar sesión';
    successMessage.value = null;
  }
};
</script>

<style scoped>
</style>
