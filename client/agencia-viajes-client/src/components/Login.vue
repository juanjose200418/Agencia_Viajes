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
import { User } from '../models/User'; // Asegúrate de que el modelo User esté configurado correctamente
import UserService from '../services/UserService'; // Importa tu servicio de usuario

const user = ref<User>(new User());
const errorMessage = ref<string | null>(null);
const successMessage = ref<string | null>(null);

// Reglas de validación
const emailRules = [
  (v: string) => !!v || 'Correo electrónico es obligatorio',
  (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido',
];

const passwordRules = [
  (v: string) => !!v || 'Contraseña es obligatoria',
  (v: string) => (v && v.length >= 6) || 'La contraseña debe tener al menos 6 caracteres',
];

const passwordMismatch = computed(() => user.value.pass !== user.value.pass); // Sin confirmación de contraseña
const formIsValid = computed(() => {
  return user.value.email && user.value.pass && !passwordMismatch.value;
});

const submitForm = async () => {
  try {
    // Cambia este método según cómo estés manejando la autenticación
    const response = await UserService.login(user.value); // Llama a tu método de login
    const { token, user: userData } = response.data; // Ajusta según la respuesta de tu backend

    // Guardar el token en el almacenamiento local
    localStorage.setItem('authToken', token);
    localStorage.setItem('user', JSON.stringify(userData)); // Opcional, almacenar el usuario

    successMessage.value = 'Inicio de sesión exitoso!';
    errorMessage.value = null;
    
    // Redirigir al usuario o realizar otras acciones necesarias
    // Por ejemplo, podrías usar `this.$router.push('/home');` si usas Vue Router
  } catch (error: unknown) {
    errorMessage.value = (error as any).message || 'Error al iniciar sesión';
    successMessage.value = null;
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
