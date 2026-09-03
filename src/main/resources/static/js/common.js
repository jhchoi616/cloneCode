(function(){
  const toggle = document.getElementById('menuToggle');
  const menu = document.getElementById('menu');
  if(toggle && menu){
    toggle.addEventListener('click', () => menu.classList.toggle('open'));
    menu.querySelectorAll('a').forEach(a => a.addEventListener('click', () => menu.classList.remove('open')));
  }
  const io = new IntersectionObserver((entries) => {
    entries.forEach(e => { if(e.isIntersecting){ e.target.classList.add('in'); io.unobserve(e.target);} });
  }, {threshold: 0.12});
  document.querySelectorAll('.reveal').forEach(el => io.observe(el));
})();
