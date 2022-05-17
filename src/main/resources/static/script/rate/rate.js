window.onload = () => {
  let starsBlocks = document.getElementsByClassName('rating-input-stars');

  function makeActive(stars, maxindex) {
    for (var i = 0; i < maxindex; i++) {
      stars[i].classList.add('active');
    }
  }

  function clearActive(stars) {
    for (var i = 0; i < stars.length; i++) {
      stars[i].classList.remove('active');
    }
  }

  for (var i = 0; i < starsBlocks.length; i++) {
    let mapState = 0;
    let currentStars = starsBlocks[i].getElementsByClassName('input-star');
    let currentInput = starsBlocks[i].getElementsByTagName("input")[0];
    let currentValue = currentInput.value;
    makeActive(currentStars, currentValue);
    mapState = currentValue;
    for (let j = 0; j < currentStars.length; j++) {
      let index = j;
      currentStars[j].addEventListener('click', (e) => {
        e.stopPropagation();
        clearActive(currentStars);
        makeActive(currentStars, index + 1);
        currentInput.value = index+1;
        mapState = index+1;
      });
      currentStars[j].addEventListener('mouseover', (e) => {
        e.stopPropagation();
        clearActive(currentStars);
        makeActive(currentStars, index + 1);
      });
      currentStars[j].addEventListener('mouseout', (e) => {
        e.stopPropagation();
        clearActive(currentStars);
        if(mapState > 0) {
          makeActive(currentStars, mapState);
        }
      });
    }
  }

}
