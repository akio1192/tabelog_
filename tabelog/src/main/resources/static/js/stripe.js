
const stripe = Stripe('pk_test_51OUjkxIiJGAUDqwuL451xS1GBH9Bie5inyR67dSSfrBxXu8vVYu60kmyNBbkvRZdVozla6iMdr4CotkNPqAseXv800d4Jrg3GE');
 const paymentButton = document.querySelector('#paymentButton');
 
paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });