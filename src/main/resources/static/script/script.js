window.onload = () => {
  let main = document.getElementById('main');
  let nav = document.getElementById('nav');
  let modal = document.getElementById('modal');
  let closeModal = document.getElementById('close');

  let allRateButtons = document.getElementsByClassName('rate-button');
  let allReadMoreButtons = document.getElementsByClassName('read-more-button');

  let allFocusButtons = [...allRateButtons, ...allReadMoreButtons];

  for (var i = 0; i < allFocusButtons.length; i++) {
    allFocusButtons[i].addEventListener('click', () => {
      nav.classList.add('unfocused');
      main.classList.add('unfocused');
      modal.classList.remove('hide');
    });
  }

  closeModal.addEventListener('click', () => {
    modal.classList.add('hide');
    nav.classList.remove('unfocused');
    main.classList.remove('unfocused');
  })

}
