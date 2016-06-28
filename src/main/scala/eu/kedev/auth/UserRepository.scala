package eu.kedev.auth



trait UserRepository {

  def authenticate(user: String, cleartextPassword: String): Boolean
}
