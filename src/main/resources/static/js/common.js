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

// 연락처/이메일 입력값 검증 및 하이픈 포맷팅
// 이메일을 먼저 뽑아내고, 남은 문자열의 숫자만 모아 전화번호 하나로 포맷한다.
// (구분자를 공백 포함으로 잡으면 "010 1234 5678"처럼 숫자 그룹을 공백으로 띄어 쓴
//  전화번호까지 쪼개져 버리므로, 공백/하이픈/점 등은 전화번호 내부 표기로 취급한다.)
function bpointFormatContact(raw) {
  const EMAIL_RE = /[^\s@]+@[^\s@]+\.[^\s@]+/g;

  function formatPhone(digits) {
    if (digits.startsWith('02')) {
      if (digits.length === 9) return digits.replace(/^(\d{2})(\d{3})(\d{4})$/, '$1-$2-$3');
      if (digits.length === 10) return digits.replace(/^(\d{2})(\d{4})(\d{4})$/, '$1-$2-$3');
      return null;
    }
    if (digits.length === 10) return digits.replace(/^(\d{3})(\d{3})(\d{4})$/, '$1-$2-$3');
    if (digits.length === 11) return digits.replace(/^(\d{3})(\d{4})(\d{4})$/, '$1-$2-$3');
    return null;
  }

  const str = String(raw || '');
  const emails = str.match(EMAIL_RE) || [];
  const digits = str.replace(EMAIL_RE, '').replace(/\D/g, '');
  const phone = digits ? formatPhone(digits) : null;

  const formatted = [];
  if (phone) formatted.push(phone);
  formatted.push(...emails);

  return { valid: formatted.length > 0, value: formatted.join(' / ') };
}
window.bpointFormatContact = bpointFormatContact;
