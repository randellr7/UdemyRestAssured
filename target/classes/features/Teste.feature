#language: pt

# ESTSEG-2468

@servicoElegibiliadade
Funcionalidade: Quero que as regras de restituição sejam cumpridas de acordo com as premissas definidas junto a Cardif


  @statusAtivoAmbosLadosElegivel
  Cenário: Status ativo em ambos os lados e elegível
    Dado que eu tenha um contrato de seguro ativo BV/Cardif
    Quando o status sinistro e cobertura da restituição for igual a Sim
    Entao sistema retorna elegebilidade positiva

  @statusAtivoAmbosLadosNaoElegivel
  Cenário: Status ativo em ambos os lados e não elegível
    Dado que eu tenha um contrato de seguro ativo BV/Cardif
    Quando o status sinistro e cobertura da restituição for igual a Nao
    Entao sistema retorna elegebilidade negativa

  @statusCanceladoAmbosLados
  Cenário: Status cancelado em ambos os lados
    Dado que eu tenha um contrato de seguro cancelado BV/Cardif
    Entao sistema retorna elegebilidade negativa

  @statusCanceladoBV
  Cenário: Status cancelado na BV e ativo na Cardif
    Dado que eu tenha um contrato de seguro cancelado na BV
    Quando eu verifico o status ativo na Cardif
    Entao sistema retorna elegebilidade negativa

  @statusCanceladoCardif
  Cenário: Status cancelado na Cardif e ativo na BV
    Dado que eu tenha um contrato de seguro ativo na BV
    Quando eu verifico o status cancelado na Cardif
    Entao sistema retorna elegebilidade negativa

  @statusAtivoDeclinadoElegivel
  Cenário: Status ativo, com codigo do Status Declinado e elegível
    Dado que eu tenha um contrato de seguro ativo BV/Cardif
    Quando status do sinistro for igual a declinado
    E motivo e cobertura da restituição for igual a Sim
    Entao sistema retorna elegebilidade positiva

  @statusAtivoDeclinadoNaoElegivel
  Cenário: Status ativo, com cdStatus Declinado e nao elegível
    Dado que eu tenha um contrato de seguro ativo BV/Cardif
    Quando status do sinistro for igual a declinado
    E motivo e cobertura da restituição for igual a Nao
    Entao sistema retorna elegebilidade negativa

  @statusAtivoEncerradoNaoElegivel
  Cenário: Status ativo, com cdStatus Declinado e nao elegível
    Dado que eu tenha um contrato de seguro ativo BV/Cardif
    Quando status do sinistro for igual a declinado
    E motivo e cobertura da restituição for igual a Nao
    Entao sistema retorna elegebilidade negativa



